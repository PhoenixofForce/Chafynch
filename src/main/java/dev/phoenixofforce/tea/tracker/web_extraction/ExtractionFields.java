package dev.phoenixofforce.tea.tracker.web_extraction;

import dev.phoenixofforce.tea.tracker.tea.TeaDTO;

import java.util.Map;
import java.util.function.BiConsumer;

public class ExtractionFields {

    public static final Map<String, BiConsumer<TeaDTO, String>> FIELD_MAPPER = Map.of(
        "title".toLowerCase(),
        TeaDTO::setName,
        "description".toLowerCase(),
        TeaDTO::setDescriptionMd,
        "harvest".toLowerCase(),
        TeaDTO::setHarvestLabel,
        "teaType".toLowerCase(),
        TeaDTO::setTeaType,
        "origin".toLowerCase(),
        TeaDTO::setOriginCity,
        "cultivar".toLowerCase(),
        TeaDTO::setCultivar);

}
