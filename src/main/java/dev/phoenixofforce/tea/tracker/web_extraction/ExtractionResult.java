package dev.phoenixofforce.tea.tracker.web_extraction;

import dev.phoenixofforce.tea.tracker.tea.TeaDTO;

import java.util.List;

public record ExtractionResult(TeaDTO teaDTO, List<ExtractionDetail> details) {
}
