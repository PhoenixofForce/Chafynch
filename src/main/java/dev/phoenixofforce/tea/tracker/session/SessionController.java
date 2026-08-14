package dev.phoenixofforce.tea.tracker.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    // Todo: add tea

    private final SessionService service;

    @GetMapping //Todo: stripped down variant
    public List<SessionDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SessionDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public SessionDto createSession(@Valid @RequestBody SessionDto dto) {
        return service.createOrUpdateSession(dto);
    }

    @PutMapping("/{id}")
    public SessionDto updateSession(@PathVariable Long id, @Valid @RequestBody SessionDto dto) {
        dto.setId(id);
        return service.createOrUpdateSession(dto);
    }
}
