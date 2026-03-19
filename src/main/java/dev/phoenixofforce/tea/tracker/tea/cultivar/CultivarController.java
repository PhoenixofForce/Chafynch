package dev.phoenixofforce.tea.tracker.tea.cultivar;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cultivars")
@RequiredArgsConstructor
public class CultivarController {

    private final CultivarRepository cultivarRepository;

    @GetMapping
    public List<Cultivar> search(@RequestParam(defaultValue = "") String q) {
        if (q.isBlank()) return cultivarRepository.findAll();
        return cultivarRepository.findByNameContainingIgnoreCase(q);
    }
}