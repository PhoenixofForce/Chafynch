package dev.phoenixofforce.tea.tracker.web_extraction;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractionDetailTest {

    @Test
    public void testMerge_bothEmpty() {
        ExtractionDetail a = new ExtractionDetail("", Optional.empty(), null);
        ExtractionDetail b = new ExtractionDetail("", Optional.empty(), null);

        ExtractionDetail result = a.merge(b);
        assertTrue(result.fieldValue().isEmpty());
    }

    @Test
    public void testMerge_bEmpty() {
        ExtractionDetail a = new ExtractionDetail("", Optional.of("a"), null);
        ExtractionDetail b = new ExtractionDetail("", Optional.empty(), null);

        ExtractionDetail result = a.merge(b);
        assertTrue(result.fieldValue().isPresent());
        assertEquals("a", result.fieldValue().get());
    }


    @Test
    public void testMerge_aEmpty() {
        ExtractionDetail a = new ExtractionDetail("", Optional.empty(), null);
        ExtractionDetail b = new ExtractionDetail("", Optional.of("b"), null);

        ExtractionDetail result = a.merge(b);
        assertTrue(result.fieldValue().isPresent());
        assertEquals("b", result.fieldValue().get());
    }

    @Test
    public void testMerge_noneEmpty() {
        ExtractionDetail a = new ExtractionDetail("", Optional.of("a"), null);
        ExtractionDetail b = new ExtractionDetail("", Optional.of("b"), null);

        ExtractionDetail result = a.merge(b);
        assertTrue(result.fieldValue().isPresent());
        assertEquals("a\nb", result.fieldValue().get());
    }

}
