package dev.phoenixofforce.tea.tracker.web_extraction;

import dev.phoenixofforce.tea.tracker.tea.TeaDTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
public class ExtractionService {

    private final RawProfileRepository repository;

    @Transactional
    public void create(ExtractionProfile profile) {
        RawProfile rawProfile = new RawProfile();
        rawProfile.setName(profile.name());
        rawProfile.setValidUrls(profile.validUrls());

        List<RawFieldSetting> settings = new ArrayList<>();
        for (ExtractionFieldSetting setting : profile.settings()) {
            RawFieldSetting rawSetting = new RawFieldSetting();
            rawSetting.setField(setting.field());
            rawSetting.setSelector(setting.selector());
            rawSetting.setRegex(setting.regex());
            rawSetting.setOperations(setting.operations());
            rawSetting.setGrabAll(setting.grabAll());

            settings.add(rawSetting);
        }

        rawProfile.setSettings(settings);
        repository.save(rawProfile);
    }

    public ExtractionResult extractTea(String url) {
        ExtractionProfile profile = findSettingsForUrl(url);
        if (profile == null) {
            return new ExtractionResult(new TeaDTO(), List.of());
        }

        TeaDTO teaDTO = new TeaDTO();
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException _) {
        }
        if ( document == null) {
            //'Todo: throw error
            return new ExtractionResult(new TeaDTO(), List.of());
        }

        List<ExtractionDetail> details = new ArrayList<>();
        for (var setting : profile.settings()) {
            BiConsumer<TeaDTO, String> fieldSetter = ExtractionFields.FIELD_MAPPER.get(setting.field().toLowerCase());
            if (fieldSetter == null) {
                details.add(new ExtractionDetail(setting.field(), Optional.empty(), List.of("Unknown field")));
                continue;
            }

            ExtractionDetail detail = extractAndApplyField(
                document,
                setting,
                result -> fieldSetter.accept(teaDTO, result));
            details.add(detail);
        }

        teaDTO.setWebsite(url);
        teaDTO.setVendor(profile.name());
        return new ExtractionResult(teaDTO, details);
    }

    private ExtractionProfile findSettingsForUrl(String url) {
        List<ExtractionProfile> allSettings = repository.findAll()
            .stream()
            .map(ExtractionProfile::from)
            .toList();

        ParsedUrl requestUrl = ParsedUrl.parse(url);
        if (requestUrl == null) return null;

        for (ExtractionProfile setting : allSettings) {
            for (String validUrl : setting.validUrls()) {
                ParsedUrl settingsUrl = ParsedUrl.parse(validUrl);
                if (settingsUrl == null) continue;

                if (requestUrl.host().equals(settingsUrl.host()) && requestUrl.path().startsWith(settingsUrl.path())) {
                    return setting;
                }
            }
        }

        return null;
    }

    ExtractionDetail extractAndApplyField(Document document, ExtractionFieldSetting settings,
        Consumer<String> consumer) {
        ExtractionDetail detail = extractField(document, settings);
        consumer.accept(detail.fieldValue().orElse(""));
        return detail;
    }

    ExtractionDetail extractField(Document document, ExtractionFieldSetting settings) {
        if (settings == null) {
            return new ExtractionDetail("Unknown", Optional.empty(), List.of("No extraction settings found"));
        }

        if (!settings.grabAll()) {
            Node node = document.selectFirst(settings.selector());
            return processNode(settings.field(), node, settings);
        }

        Elements elements = document.select(settings.selector());
        if (elements.isEmpty()) {
            return new ExtractionDetail(settings.field(), Optional.empty(), List.of("No starting fields found"));
        }
        return elements.stream()
            .map(e -> processNode(settings.field(), e, settings))
            .reduce(new ExtractionDetail(settings.field(), Optional.empty(), List.of()), ExtractionDetail::merge);
    }

    private ExtractionDetail processNode(String field, Node node, ExtractionFieldSetting settings) {
        List<String> errors = new ArrayList<>();

        node = advanceNode(node, settings.operations(), errors);
        if (node == null) return new ExtractionDetail(field, Optional.empty(), errors);

        String text = "";
        if (node instanceof TextNode textNode) {
            text = textNode.text();
        } else if (node instanceof Element element) {
            text = element.text();
        }

        text = extractRegex(text, settings.regex(), errors);
        text = text.replace('\u00A0', ' ').trim(); //nbsp
        if (text.isBlank()) {
            errors.add("Text came back empty");
            return new ExtractionDetail(field, Optional.empty(), errors);
        }

        return new ExtractionDetail(field, Optional.of(text), errors);
    }

    private Node advanceNode(Node node, List<String> operations, List<String> errors) {
        if (node == null) {
            errors.add("No starting node found");
            return node;
        }
        if (operations == null) {
            return node;
        }

        for (int i = 0; i < operations.size(); i++) {
            String operation = operations.get(i);

            if ("nextSibling".equals(operation)) node = node.nextSibling();
            else if ("nextElementSibling".equals(operation)) node = node.nextElementSibling();
            else errors.add("Unknown operation " + i + ": " + operation);

            if (node == null) {
                errors.add("No node found for operation " + i + ": " + operation);
                return null;
            }
        }
        return node;
    }

    private String extractRegex(String text, String regexMatcher, List<String> errors) {
        if (regexMatcher == null || regexMatcher.isBlank()) return text;

        try {
            Pattern pattern = Pattern.compile(regexMatcher);
            Matcher matcher = pattern.matcher(text);
            if (!matcher.find()) {
                errors.add("Regex could not be found");
                return text;
            }

            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) == null) continue;
                return matcher.group(i);
            }
            return matcher.group(0);
        } catch (PatternSyntaxException _) {
            errors.add("Regex could not be parsed: " + regexMatcher);
            return text;
        }
    }

    private record ParsedUrl(String host, String path) {

        private static ParsedUrl parse(String url) {
            String host = null;
            String path = null;
            try {
                URI uri = new URI(url);
                host = uri.getHost();
                path = uri.getPath();
            } catch (URISyntaxException _) {
            }

            if ( host == null) return null;
            if (path == null) path = "";

            host = host.toLowerCase();
            if (!path.endsWith("/")) {
                path += "/";
            }

            return new ParsedUrl(host, path);
        }
    }

}
