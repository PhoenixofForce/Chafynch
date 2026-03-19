package dev.phoenixofforce.tea.tracker.tea.type;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tea-types")
@RequiredArgsConstructor
public class TeaTypeController {

    private final TeaTypeRepository teaTypeRepository;

    @GetMapping
    public List<TeaType> findAll() {
        return teaTypeRepository.findAll();
    }
}