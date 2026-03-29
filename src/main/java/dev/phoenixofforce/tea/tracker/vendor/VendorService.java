package dev.phoenixofforce.tea.tracker.vendor;

import dev.phoenixofforce.tea.tracker.tea.Tea;
import dev.phoenixofforce.tea.tracker.tea.TeaDTO;
import dev.phoenixofforce.tea.tracker.tea.TeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

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
}