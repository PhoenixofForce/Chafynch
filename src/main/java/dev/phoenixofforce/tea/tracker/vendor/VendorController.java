package dev.phoenixofforce.tea.tracker.vendor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorRepository vendorRepository;

    @GetMapping
    public List<Vendor> search(@RequestParam(defaultValue = "") String q) {
        if (q.isBlank()) return vendorRepository.findAll();
        return vendorRepository.findByNameContainingIgnoreCase(q);
    }
}