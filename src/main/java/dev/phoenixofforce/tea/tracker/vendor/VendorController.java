package dev.phoenixofforce.tea.tracker.vendor;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public List<VendorOverviewDto> search(@RequestParam(defaultValue = "") String q) {
        return vendorService.find(q);
    }
}