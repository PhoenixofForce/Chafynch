package dev.phoenixofforce.tea.tracker.tea;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/teas")
@RequiredArgsConstructor
public class TeaController {

    private final TeaService teaService;

    @GetMapping
    public List<TeaDTO> findAll() {
        return teaService.findAll();
    }

    @GetMapping("/{id}")
    public TeaDTO findById(@PathVariable Long id) {
        return teaService.findById(id);
    }

    @PostMapping
    public TeaDTO create(@Valid @RequestBody TeaDTO dto) {
        return teaService.create(dto);
    }

    @PutMapping("/{id}")
    public TeaDTO update(@PathVariable long id, @Valid @RequestBody TeaDTO dto) {
        return teaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teaService.delete(id);
    }
}
