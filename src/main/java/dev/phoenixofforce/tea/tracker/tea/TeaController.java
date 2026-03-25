package dev.phoenixofforce.tea.tracker.tea;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/teas")
@RequiredArgsConstructor
public class TeaController {

    private final TeaService teaService;

    @GetMapping
    public List<TeaDTO> findAll() {
        return teaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeaDTO create(@Valid @RequestBody TeaDTO dto) {
        return teaService.create(dto);
    }
}