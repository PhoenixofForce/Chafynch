package dev.phoenixofforce.tea.tracker.web_extraction;

import java.util.List;

public record ExtractionProfile(
        String name,
        List<String> validUrls,
        List<ExtractionFieldSetting> settings
) {

    public static ExtractionProfile from(RawProfile rawProfile) {
        List<ExtractionFieldSetting> settings = rawProfile.getSettings().stream().map(rawSetting ->
             new ExtractionFieldSetting(
                    rawSetting.getField(),
                    rawSetting.getSelector(),
                    rawSetting.getRegex(),
                    rawSetting.getOperations(),
                    rawSetting.isGrabAll()
            )).toList();

        return new ExtractionProfile(
                rawProfile.getName(),
                rawProfile.getValidUrls(),
                settings
        );
    }

}
