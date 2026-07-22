package dev.phoenixofforce.tea.tracker.tea.cultivar;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public CultivarDto create(@RequestParam String name) {
        return CultivarDto.from(cultivarService.resolveOrCreate(name));
    }

    @PutMapping("/{id}")
    public CultivarDto update(@PathVariable long id, @RequestBody CultivarDto cultivarDto) {
        return cultivarService.update(id, cultivarDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cultivarService.delete(id);
    }
}
