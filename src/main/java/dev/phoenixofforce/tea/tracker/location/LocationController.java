package dev.phoenixofforce.tea.tracker.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationDto> search(@RequestParam(defaultValue = "") String q) {
        return locationService.find(q);
    }
}