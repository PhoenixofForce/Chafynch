package dev.phoenixofforce.tea.tracker.web_extraction;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extract")
@RequiredArgsConstructor
public class ExtractionController {

    private final ExtractionService service;

    @GetMapping("/all")
    public List<ExtractionProfile> getAllProfiles() {
        return service.getAllProfiles();
    }

    @PostMapping
    public void create(@RequestBody ExtractionProfile profile) {
        service.create(profile);
    }

    @GetMapping
    public ExtractionResult extract(@RequestParam String url) {
        return service.extractTea(url);
    }

    @PostMapping("/test")
    public ExtractionResult extract(@RequestParam String url, @RequestBody ExtractionProfile profile) {
        return service.extractTea(url, profile);
    }

}
