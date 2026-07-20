package dev.phoenixofforce.tea.tracker.web_extraction;

import java.util.List;

public record ExtractionFieldSetting(String jsoupSelector, String regexMatcher, List<String> operations, boolean grabAll) {
}
