package dev.phoenixofforce.tea.tracker.web_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtractionServiceTest {

    private static ExtractionService service;

    @BeforeAll
    static void setUp() {
        service = new ExtractionService();
    }

    @Test
    void extractBasicField() {
        Document document = Jsoup.parse("""
            <div class="target"> Success </div>
        """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting(".target", null, List.of(), false);
        ExtractionDetail detail = service.extractField("", document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success", detail.fieldValue().get());
    }

    @Test
    void extractBasicFieldUsingRegex() {
        Document document = Jsoup.parse("""
            <div class="target"> Target: Success </div>
        """);

        ExtractionFieldSetting setting = new ExtractionFieldSetting(".target", "(?:Target: )(.*)", List.of(), false);
        ExtractionDetail detail = service.extractField("", document, setting);

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

        ExtractionFieldSetting setting = new ExtractionFieldSetting("div:containsOwn(target)", null, List.of("nextSibling"), false);
        ExtractionDetail detail = service.extractField("", document, setting);

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

        ExtractionFieldSetting setting = new ExtractionFieldSetting("div:containsOwn(target)", null, List.of("nextElementSibling"), false);
        ExtractionDetail detail = service.extractField("", document, setting);

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

        ExtractionFieldSetting setting = new ExtractionFieldSetting(".target", null, List.of(), true);
        ExtractionDetail detail = service.extractField("", document, setting);

        assertTrue(detail.fieldValue().isPresent());
        assertTrue(detail.errors().isEmpty());
        assertEquals("Success 1\nSuccess 2", detail.fieldValue().get());
    }

}
