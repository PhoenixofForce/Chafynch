package dev.phoenixofforce.tea.tracker.web_extraction;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ExtractionProfile(
    @NotNull long id,
    String name,
    List<String> validUrls,
    List<ExtractionFieldSetting> settings) {

    public static ExtractionProfile from(RawProfile rawProfile) {
        List<ExtractionFieldSetting> settings = rawProfile.getSettings().stream()
            .map(
                rawSetting -> new ExtractionFieldSetting(
                    rawSetting.getField(),
                    rawSetting.getSelector(),
                    rawSetting.getRegex(),
                    rawSetting.getOperations(),
                    rawSetting.isGrabAll()))
            .toList();

        return new ExtractionProfile(
            rawProfile.getId(),
            rawProfile.getName(),
            rawProfile.getValidUrls(),
            settings);
    }

    public RawProfile apply(RawProfile entity) {
        entity.setName(this.name());
        entity.setValidUrls(this.validUrls());

        List<RawFieldSetting> settings = new ArrayList<>();
        for (ExtractionFieldSetting setting : this.settings()) {
            RawFieldSetting rawSetting = new RawFieldSetting();
            rawSetting.setField(setting.field());
            rawSetting.setSelector(setting.selector());
            rawSetting.setRegex(setting.regex());
            rawSetting.setOperations(setting.operations());
            rawSetting.setGrabAll(setting.grabAll());

            settings.add(rawSetting);
        }

        entity.getSettings().clear();
        entity.getSettings().addAll(settings);
        return entity;
    }

}
