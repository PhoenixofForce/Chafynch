package dev.phoenixofforce.tea.tracker.geocoding;

import java.util.Optional;

public interface GeocodingService {
    Optional<GeocodingResult> geocode(String country, String province, String city);

    /**
     * Tries geocoding with full specificity first (city+province+country),
     * then falls back to broader queries (province+country, then country only).
     */
    Optional<GeocodingResult> coarsenedGeocode(String country, String province, String city);
}