package dev.phoenixofforce.tea.tracker.location;

import java.util.List;

public record LocationDto(String city, String province, String country, Double latitude, Double longitude) {

    public static LocationDto from(Location location) {
        if(location == null) return null;
        return new LocationDto(location.getCity(), location.getProvince(), location.getCountry(),  location.getLatitude(), location.getLongitude());
    }

    public static List<LocationDto> from(List<Location> locations) {
        return locations.stream()
                .map(LocationDto::from)
                .toList();
    }
}
