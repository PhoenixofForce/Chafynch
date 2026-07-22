package dev.phoenixofforce.tea.tracker.web_extraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class ExtractionServiceTest {

    private static ExtractionService service;

    @BeforeAll
    static void setUp() {
        service = new ExtractionService(null);
    }

    @Test
    void extractBasicField() {
        Document document = Jsoup.parse("""
                <div class="target"> Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of(), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingRegex() {
        Document document = Jsoup.parse("""
                <div class="target"> Target: Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting(
            "",
            ".target",
            "(?:Target: )(.*)",
            List.of(),
            false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingRegexFallback() {
        Document document = Jsoup.parse("""
                <div class="target"> t 1234 t </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", "\\d+", List.of(), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("1234", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingRegexLaterGroup() {
        Document document = Jsoup.parse("""
                <div class="target"> Target: Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting(
            "",
            ".target",
            "(Fail)|(Success)",
            List.of(),
            false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingNextSibling() {
        Document document = Jsoup.parse("""
                <div>
                    <div> Target </div> Success
                </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting(
            "",
            "div:containsOwn(target)",
            null,
            List.of("nextSibling"),
            false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingNextElementSibling() {
        Document document = Jsoup.parse("""
                <div>
                    <div> Target </div>
                    <div> Success </div>
                </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting(
            "",
            "div:containsOwn(target)",
            null,
            List.of("nextElementSibling"),
            false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingGrabAll() {
        Document document = Jsoup.parse("""
                <div>
                    <div class="target"> Success 1 </div>
                    <div class="target"> Success 2 </div>
                </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of(), true);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success 1\nSuccess 2", detail.fieldValue().get());
    }

    @Test
    void returnError_withoutSettings() {
        Document document = Jsoup.parse("""
                <div>
                    <div class="target"> Success 1 </div>
                    <div class="target"> Success 2 </div>
                </div>
            """);

        ExtractionDetail detail = service.extractField(document, null);

        assertTrue(detail.fieldValue().isEmpty());
        assertEquals(1, detail.errors().size());
        assertEquals("No extraction settings found", detail.errors().getFirst());
    }

    @Test
    void returnError_withoutGroups() {
        Document document = Jsoup.parse("""
                <div>
                    <div class="no-target"> Success 1 </div>
                    <div class="no-target"> Success 2 </div>
                </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of(), true);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isEmpty());
        assertEquals(1, detail.errors().size());
        assertEquals("No starting fields found", detail.errors().getFirst());
    }

    @Test
    void returnError_whenFieldIsEmpty() {
        Document document = Jsoup.parse("""
                <div class="target">  </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of(), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isEmpty());
        assertEquals(1, detail.errors().size());
        assertEquals("Text came back empty", detail.errors().getFirst());
    }

    @Test
    void returnError_withoutGroup() {
        Document document = Jsoup.parse("""
                <div class="no-target"> Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of(), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isEmpty());
        assertEquals(1, detail.errors().size());
        assertEquals("No starting node found", detail.errors().getFirst());
    }

    @Test
    void returnError_withUnknownOperation() {
        Document document = Jsoup.parse("""
                <div class="target"> Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of("foo"), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertEquals(1, detail.errors().size());
        assertEquals("Unknown operation 0: foo", detail.errors().getFirst());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "nextSibling", "nextElementSibling"
    })
    void returnError_withInvalidOperation(String operation) {
        Document document = Jsoup.parse("""
                <html><div class="target">Success</div></html>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", null, List.of(operation), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isEmpty());
        assertEquals(1, detail.errors().size());
        assertEquals("No node found for operation 0: " + operation, detail.errors().getFirst());
    }

    @Test
    void returnError_withoutRegexPresent() {
        Document document = Jsoup.parse("""
                <div class="target"> Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", "[A-Z][A-Z]+", List.of(), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertEquals("Success", detail.fieldValue().get());
        assertEquals(1, detail.errors().size());
        assertEquals("Regex could not be found", detail.errors().getFirst());
    }

    @Test
    void returnError_withInvalidRegex() {
        Document document = Jsoup.parse("""
                <div class="target"> Success </div>
            """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting("", ".target", "[A-Z][A-Z", List.of(), false);
        ExtractionDetail detail = service.extractField(document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertEquals("Success", detail.fieldValue().get());
        assertEquals(1, detail.errors().size());
        assertEquals("Regex could not be parsed: [A-Z][A-Z", detail.errors().getFirst());
    }

}
