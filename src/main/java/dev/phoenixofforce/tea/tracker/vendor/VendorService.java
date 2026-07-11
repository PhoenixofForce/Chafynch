package dev.phoenixofforce.tea.tracker.vendor;

import dev.phoenixofforce.tea.tracker.location.LocationDto;
import dev.phoenixofforce.tea.tracker.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;
    private final LocationService locationService;

    public List<VendorOverviewDto> find(String query) {
        return vendorRepository.findVendorOverviews(query)
                .stream()
                .map(vendorOverview -> new VendorOverviewDto(VendorDto.from(vendorOverview.getVendor()), vendorOverview.getAveragePricePerGram(), vendorOverview.getTeaCount()))
                .toList();
    }

    public Vendor resolveOrCreate(String name) {
        return vendorRepository.findByName(name)
                .orElseGet(() -> {
                    Vendor v = new Vendor();
                    v.setName(name);
                    return vendorRepository.save(v);
                });
    }

    @Transactional
    public VendorDto create(VendorDto vendorDto) {
        Vendor vendor = new Vendor();
        vendor.setName(vendorDto.name());
        vendor.setWebsite(vendorDto.website());

        LocationDto locationDto = vendorDto.locationDto();
        if(locationDto != null) {
            vendor.setLocation(locationService.resolveOrCreate(locationDto.country(), locationDto.province(), locationDto.city()));

        }

        vendor = vendorRepository.save(vendor);
        return VendorDto.from(vendor);
    }

    @Transactional
    public VendorDto update(long id, VendorDto vendorDto) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor Not Found"));
        vendor.setName(vendorDto.name());
        vendor.setWebsite(vendorDto.website());

        LocationDto locationDto = vendorDto.locationDto();
        if(locationDto != null) {
            vendor.setLocation(locationService.resolveOrCreate(locationDto.country(), locationDto.province(), locationDto.city()));

        }

        vendor = vendorRepository.save(vendor);
        return VendorDto.from(vendor);
    }

    @Transactional
    public void delete(long id) {
       if(!vendorRepository.existsById(id)) {
           throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor Not Found");
       }
       vendorRepository.deleteById(id);
       vendorRepository.flush();
    }
}
