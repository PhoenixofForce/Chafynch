package dev.phoenixofforce.tea.tracker.vendor;

import dev.phoenixofforce.tea.tracker.location.LocationDto;

import java.util.List;

public record VendorDto(Long id, String name, String website, LocationDto locationDto) {

    public static VendorDto from(Vendor vendor) {
        if(vendor == null) return null;
        return new VendorDto(vendor.getId(), vendor.getName(), vendor.getWebsite(), LocationDto.from(vendor.getLocation()));
    }

    public static List<VendorDto> from(List<Vendor> vendors) {
        return vendors.stream().map(VendorDto::from).toList();
    }
}
