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

    private final SessionService service;

    // Todo: stripped down variant
    // Todo: add for by tea
    @GetMapping
    public List<SessionDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SessionDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/{teaId}")
    public SessionDto createSession(@PathVariable Long teaId, @Valid @RequestBody SessionDto dto) {
        return service.createOrUpdateSession(teaId, dto);
    }

    @PutMapping("/{id}")
    public SessionDto updateSession(@PathVariable Long id, @Valid @RequestBody SessionDto dto) {
        dto.setId(id);
        return service.createOrUpdateSession(null, dto);
    }
}
