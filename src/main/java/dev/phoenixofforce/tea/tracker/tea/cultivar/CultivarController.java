package dev.phoenixofforce.tea.tracker.tea.cultivar;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/cultivars")
@RequiredArgsConstructor
public class CultivarController {

    private final CultivarService cultivarService;

    @GetMapping
    public List<CultivarDto> search(@RequestParam(defaultValue = "") String q) {
        return cultivarService.searchByName(q);
    }
}