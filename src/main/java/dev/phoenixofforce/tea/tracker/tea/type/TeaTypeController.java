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
}