package dev.phoenixofforce.tea.tracker.session.tasting_note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasting-notes")
@RequiredArgsConstructor
public class TastingNoteController {

    private final TastingNoteService service;

    @GetMapping
    public List<String> findNotes(@RequestParam(defaultValue = "") String query,
        @RequestParam(defaultValue = "10") int limit) {
        return service.findTastingNotes(query, limit);
    }

}
