package dev.phoenixofforce.tea.tracker.vendor;

import jakarta.validation.constraints.NotNull;

public record VendorOverviewDto(@NotNull VendorDto vendor, Double averagePricePerGram, Long teas) {

}
