package dev.phoenixofforce.tea.tracker.web_extraction;

import dev.phoenixofforce.tea.tracker.tea.TeaDTO;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class ExtractionFields {

    public static final Map<String, BiConsumer<TeaDTO, String>> FIELD_MAPPER = Map.of(
        "title".toLowerCase(Locale.ROOT),
        TeaDTO::setName,
        "description".toLowerCase(Locale.ROOT),
        TeaDTO::setDescriptionMd,
        "harvest".toLowerCase(Locale.ROOT),
        TeaDTO::setHarvestLabel,
        "teaType".toLowerCase(Locale.ROOT),
        TeaDTO::setTeaType,
        "origin".toLowerCase(Locale.ROOT),
        TeaDTO::setOriginCity,
        "cultivar".toLowerCase(Locale.ROOT),
        TeaDTO::setCultivar);

}
