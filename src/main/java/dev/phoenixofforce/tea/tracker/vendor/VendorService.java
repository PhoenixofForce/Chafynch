package dev.phoenixofforce.tea.tracker.vendor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public Vendor resolveOrCreate(String name) {
        return vendorRepository.findByName(name)
                .orElseGet(() -> {
                    Vendor v = new Vendor();
                    v.setName(name);
                    return vendorRepository.save(v);
                });
    }
}