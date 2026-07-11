package dev.phoenixofforce.tea.tracker.tea.cultivar;

import dev.phoenixofforce.tea.tracker.tea.TeaDTO;
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

    @PostMapping
    public CultivarDto create(@RequestParam String name) {
        return CultivarDto.from(cultivarService.resolveOrCreate(name));
    }

    @PutMapping
    public CultivarDto update(@RequestBody CultivarDto cultivarDto) {
        return cultivarService.update(cultivarDto);
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        cultivarService.delete(id);
    }
}