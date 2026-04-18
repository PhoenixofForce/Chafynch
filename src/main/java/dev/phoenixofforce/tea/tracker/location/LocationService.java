package dev.phoenixofforce.tea.tracker.location;

import dev.phoenixofforce.tea.tracker.geocoding.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService;

    public List<LocationDto> find(String query) {
        List<Location> locations;
        if (query.isBlank()) locations = locationRepository.findAll();
        else locations = locationRepository.search(query);
        return LocationDto.from(locations);
    }

    public Location resolveOrCreate(String country, String province, String city) {
        if(country == null || province == null || city == null) return null;
        if(country.isBlank() && province.isBlank() && city.isBlank()) return null;

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