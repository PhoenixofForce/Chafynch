package dev.phoenixofforce.tea.tracker.session.tasting_note;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TastingNoteService {

    private final TastingNoteRepository repository;

    @Transactional(readOnly = true)
    public List<String> findTastingNotes(String query, int limit) {
        return repository.findNotes(query, Limit.of(limit));
    }

    @Transactional
    public Map<String, TastingNote> resolveOrCreateAll(Set<String> notes) {
        Map<String, TastingNote> map = new HashMap<>();
        repository.findAllByNoteIn(notes).forEach(e -> map.put(e.getNote(), e));

        List<TastingNote> missing = notes.stream()
            .filter(note -> !map.containsKey(note))
            .map(e -> {
                TastingNote note = new TastingNote();
                note.setNote(e);
                return note;
            })
            .toList();

        repository.saveAll(missing)
            .forEach(e -> map.put(e.getNote(), e));

        return map;
    }

}
