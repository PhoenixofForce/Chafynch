package dev.phoenixofforce.tea.tracker.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository locationRepository;

    @GetMapping
    public List<Location> search(@RequestParam(defaultValue = "") String q) {
        if (q.isBlank()) return locationRepository.findAll();
        return locationRepository.search(q);
    }
}