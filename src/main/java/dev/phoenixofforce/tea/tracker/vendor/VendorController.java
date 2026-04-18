package dev.phoenixofforce.tea.tracker.vendor;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
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

    @PostMapping
    public VendorDto save(@Valid @RequestBody VendorDto vendorDto) {
        return vendorService.create(vendorDto);
    }

    @PutMapping("/{id}")
    public VendorDto update(@PathVariable long id, @Valid @RequestBody VendorDto vendorDto) {
        return vendorService.update(id, vendorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        vendorService.delete(id);
    }
}