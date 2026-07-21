package dev.phoenixofforce.tea.tracker.web_extraction;

import java.util.List;

public record ExtractionFieldSetting(String field, String selector, String regex, List<String> operations, boolean grabAll) {
}
