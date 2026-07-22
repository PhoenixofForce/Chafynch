package dev.phoenixofforce.tea.tracker.geocoding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class NominatimGeocodingService implements GeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org";

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    public NominatimGeocodingService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder
            .baseUrl(NOMINATIM_URL)
            .defaultHeader("User-Agent", "TeaTracker/1.0")
            .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<GeocodingResult> coarsenedGeocode(String country, String province, String city) {
        Optional<GeocodingResult> result = geocode(country, province, city);

        if (result.isEmpty() && (province != null || city != null)) {
            result = geocode(country, province, null);
        }

        if (result.isEmpty() && province != null) {
            result = geocode(country, null, null);
        }

        return result;
    }

    @Override
    public Optional<GeocodingResult> geocode(String country, String province, String city) {
        String query = Stream.of(city, province, country)
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", "));

        if (query.isBlank()) {
            return Optional.empty();
        }

        try {
            String json = restClient.get()
                .uri(
                    uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .body(String.class);

            List<Map<String, Object>> results = objectMapper.readValue(json, new TypeReference<>() {});

            if (results == null || results.isEmpty()) {
                log.warn("Geocoding had no result for query '{}'", query);
                return Optional.empty();
            }

            Map<String, Object> first = results.getFirst();
            double lat = Double.parseDouble((String) first.get("lat"));
            double lon = Double.parseDouble((String) first.get("lon"));
            return Optional.of(new GeocodingResult(lat, lon));
        } catch (Exception e) {
            log.warn("Geocoding failed for query '{}': {}", query, e.getMessage());
            return Optional.empty();
        }
    }
}
