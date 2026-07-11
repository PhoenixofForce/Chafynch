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

    @PutMapping("/{id}")
    public TeaTypeDto update(@PathVariable long id, @RequestBody TeaTypeDto teaTypeDto) {
        return teaTypeService.update(id, teaTypeDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teaTypeService.delete(id);
    }
}