package dev.phoenixofforce.tea.tracker.web_extraction;

public record ExtractionSettings(
        ExtractionFieldSetting title,
        ExtractionFieldSetting cultivar,
        ExtractionFieldSetting teaType,
        ExtractionFieldSetting description,
        ExtractionFieldSetting harvest,
        ExtractionFieldSetting origin // or as three separate ones?
) {

}
