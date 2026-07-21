package dev.phoenixofforce.tea.tracker.web_extraction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/extract")
@RequiredArgsConstructor
public class ExtractionController {

    private final ExtractionService extractionService;

    @PostMapping
    public void create(@RequestBody ExtractionProfile profile) {
        extractionService.create(profile);
    }

    @GetMapping
    public ExtractionResult extract(@RequestParam String url) {
        return extractionService.extractTea(url);
    }

}
