package dev.phoenixofforce.tea.tracker.location;

import dev.phoenixofforce.tea.tracker.geocoding.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService;

    public Location resolveOrCreate(String country, String province, String city) {
        return locationRepository.findByCountryAndProvinceAndCity(country, province, city)
                .orElseGet(() -> {
                    Location l = new Location();
                    l.setCountry(country);
                    l.setProvince(province);
                    l.setCity(city);

                    geocodingService.coarsenedGeocode(country, province, city)
                            .ifPresent(result -> {
                                l.setLatitude(result.latitude());
                                l.setLongitude(result.longitude());
                            });

                    return locationRepository.save(l);
                });
    }
}