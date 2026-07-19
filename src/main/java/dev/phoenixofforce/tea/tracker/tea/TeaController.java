package dev.phoenixofforce.tea.tracker.tea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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
    @ResponseStatus(HttpStatus.CREATED)
    public TeaDTO create(@Valid @RequestBody TeaDTO dto) {
        return teaService.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeaDTO update(@PathVariable long id, @Valid @RequestBody TeaDTO dto) {
        return teaService.update(id, dto);
    }
}