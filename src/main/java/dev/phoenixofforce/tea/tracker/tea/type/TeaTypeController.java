package dev.phoenixofforce.tea.tracker.tea.type;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/tea-types")
@RequiredArgsConstructor
public class TeaTypeController {

    private final TeaTypeService teaTypeService;

    @GetMapping
    public List<TeaTypeDto> findAll() {
        return teaTypeService.findAll();
    }

    @PostMapping
    public TeaTypeDto create(@RequestParam String name) {
        return TeaTypeDto.from(teaTypeService.create(name));
    }

    @PutMapping
    public TeaTypeDto update(@RequestBody TeaTypeDto teaTypeDto) {
        return teaTypeService.update(teaTypeDto);
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        teaTypeService.delete(id);
    }
}