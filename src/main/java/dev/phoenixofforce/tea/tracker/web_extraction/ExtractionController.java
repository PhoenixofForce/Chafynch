package dev.phoenixofforce.tea.tracker.web_extraction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/extract")
@RequiredArgsConstructor
public class ExtractionController {

    private final ExtractionService extractionService;

    @GetMapping
    public ExtractionResult extract(@RequestParam String url) {
        return extractionService.extractTea(url);
    }

}
