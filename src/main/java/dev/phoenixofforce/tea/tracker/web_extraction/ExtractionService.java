package dev.phoenixofforce.tea.tracker.web_extraction;

import dev.phoenixofforce.tea.tracker.tea.TeaDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
public class ExtractionService {

    public ExtractionResult extractTea(String url) throws IOException {
        ExtractionSettings settings = findSettingsForUrl(url);
        if (settings == null) {
            return new ExtractionResult(new TeaDTO(), List.of());
        }

        TeaDTO teaDTO = new TeaDTO();
        Document document = Jsoup.connect(url).get();

        ExtractionDetail title = extractField("title", document, settings.title());
        ExtractionDetail description = extractField("description", document, settings.description());
        ExtractionDetail cultivar = extractField("cultivar", document, settings.cultivar());
        ExtractionDetail teaType = extractField("teaType", document, settings.teaType());
        ExtractionDetail harvest = extractField("harvest", document, settings.harvest());
        ExtractionDetail origin = extractField("origin", document, settings.origin());

        teaDTO.setWebsite(url);
        teaDTO.setName(title.fieldValue().orElse(""));
        teaDTO.setDescriptionMd(description.fieldValue().orElse(""));
        teaDTO.setCultivar(cultivar.fieldValue().orElse(""));
        teaDTO.setTeaType(teaType.fieldValue().orElse(""));
        teaDTO.setHarvestLabel(harvest.fieldValue().orElse(""));
        teaDTO.setOriginCity(origin.fieldValue().orElse(""));

        return new ExtractionResult(
                teaDTO,
                List.of(title, description, cultivar, teaType, harvest, origin)
        );
    }

    private ExtractionSettings findSettingsForUrl(String url) {
        return null; // Todo:
    }

    ExtractionDetail extractField(String field, Document document, ExtractionFieldSetting settings) {
        if(settings == null) {
            return new ExtractionDetail(field, Optional.empty(), List.of());
        }

        if(!settings.grabAll()) {
            Node node = document.selectFirst(settings.jsoupSelector());
            return processNode(field, node, settings);
        }

        Elements elements = document.select(settings.jsoupSelector());
        if(elements.isEmpty()) {
            return new ExtractionDetail(field, Optional.empty(), List.of("No starting fields found"));
        }
        return elements.stream()
                .map(e -> processNode(field, e, settings))
                .reduce(new ExtractionDetail(field, Optional.empty(), List.of()), ExtractionDetail::merge);
    }

    private ExtractionDetail processNode(String field, Node node, ExtractionFieldSetting settings) {
        List<String> errors = new ArrayList<>();

        node = advanceNode(node, settings.operations(), errors);
        if(node == null) return new ExtractionDetail(field, Optional.empty(), errors);

        String text = "";
        if(node instanceof TextNode textNode) {
            text = textNode.text();
        } else if(node instanceof Element element) {
            text = element.text();
        }

        text = extractRegex(text, settings.regexMatcher(), errors);
        text = text.replace('\u00A0', ' ').trim(); //nbsp
        if(text.isBlank()) {
            errors.add("Text came back empty");
            return new ExtractionDetail(field, Optional.empty(), errors);
        }

        return new ExtractionDetail(field, Optional.of(text), errors);
    }

    private Node advanceNode(Node node, List<String> operations, List<String> errors) {
        if(node == null) {
            errors.add("No starting node found");
            return node;
        }
        if(operations == null) {
            return node;
        }

        for(int i = 0; i < operations.size(); i++) {
            String operation = operations.get(i);

            if("nextSibling".equals(operation)) node = node.nextSibling();
            else if("nextElementSibling".equals(operation)) node = node.nextElementSibling();
            else errors.add("Unknown operation "+ i + ": " + operation);

            if(node == null) {
                errors.add("No node found for operation " + i + ": " + operation);
                return null;
            }
        }
        return node;
    }

    private String extractRegex(String text, String regexMatcher, List<String> errors) {
        if(regexMatcher == null || regexMatcher.isBlank()) return text;

        try {
            Pattern pattern = Pattern.compile(regexMatcher);
            Matcher matcher = pattern.matcher(text);
            if(!matcher.find()) {
                errors.add("Regex could not be found");
                return text;
            }

            for(int i = 1; i <= matcher.groupCount(); i++) {
                if(matcher.group(i) == null) continue;
                return matcher.group(i);
            }
            return matcher.group(0);
        } catch (PatternSyntaxException _) {
            errors.add("Regex could not be parsed: " + regexMatcher);
            return text;
        }
    }

}
