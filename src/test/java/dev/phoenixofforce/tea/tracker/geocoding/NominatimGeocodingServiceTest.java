package dev.phoenixofforce.tea.tracker.geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Manual integration test — calls the live Nominatim API")
class NominatimGeocodingServiceTest {

    private static NominatimGeocodingService service;

    @BeforeAll
    static void setUp() {
        service = new NominatimGeocodingService(RestClient.builder(), new ObjectMapper());
    }

    @Test
    void geocodeWithCountryOnly() {
        Optional<GeocodingResult> result = service.geocode("Japan", null, null);
        printResult("Japan, null, null", result);

        assertTrue(result.isPresent());
        assertEquals(36, result.get().latitude(), 5);
        assertEquals(138, result.get().longitude(), 5);
    }

    @Test
    void geocodeWithCountryAndProvince() {
        Optional<GeocodingResult> result = service.geocode("India", "Darjeeling", null);
        printResult("India, Darjeeling, null", result);

        assertTrue(result.isPresent());
        assertEquals(27, result.get().latitude(), 1);
        assertEquals(88, result.get().longitude(), 1);
    }

    @Test
    void geocodeWithAllFields() {
        Optional<GeocodingResult> result = service.geocode("China", "Fujian", "Wuyishan");
        printResult("China, Fujian, Wuyishan", result);

        assertTrue(result.isPresent());
        assertEquals(27.7, result.get().latitude(), 1);
        assertEquals(118, result.get().longitude(), 1);
    }

    @Test
    void geocodeWithCityOnly() {
        Optional<GeocodingResult> result = service.geocode("Sri Lanka", null, "Nuwara Eliya");
        printResult("Sri Lanka, null, Nuwara Eliya", result);

        assertTrue(result.isPresent());
    }

    @Test
    void geocodeWithBlankInputsReturnsEmpty() {
        Optional<GeocodingResult> result = service.geocode("", null, "");
        printResult("'', null, ''", result);

        assertTrue(result.isEmpty());
    }

    @Test
    void geocodeWithNonsenseReturnsEmptyOrResult() {
        Optional<GeocodingResult> result = service.geocode("Xyzzyplugh", null, null);
        printResult("Xyzzyplugh, null, null", result);
        // no assertion — just observing what Nominatim returns for garbage input
    }

    private void printResult(String query, Optional<GeocodingResult> result) {
        String value = result
                .map(r -> "lat=%.4f, lon=%.4f".formatted(r.latitude(), r.longitude()))
                .orElse("empty");
        System.out.println("[geocode] %s => %s".formatted(query, value));
    }
}