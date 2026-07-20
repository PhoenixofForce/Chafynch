package dev.phoenixofforce.tea.tracker.web_extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ExtractionDetail(String fieldName, Optional<String> fieldValue, List<String> errors) {

    public ExtractionDetail merge(ExtractionDetail detail) {

        Optional<String> newValue;
        if(fieldValue.isEmpty()) {
            newValue = detail.fieldValue;
        } else if(detail.fieldValue.isEmpty()) {
            newValue = fieldValue;
        } else {
            newValue = Optional.of(fieldValue.get() + "\n"  + detail.fieldValue.get());
        }

        List<String> newErrors = new ArrayList<>();
        newErrors.addAll(errors);
        newErrors.addAll(detail.errors);

        return new ExtractionDetail(fieldName, newValue, newErrors);
    }

}
