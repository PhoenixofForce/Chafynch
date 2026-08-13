package dev.phoenixofforce.tea.tracker.session.tasting_note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TastingNoteRepository extends JpaRepository<TastingNote, Long> {

    List<TastingNote> findAllByNoteIn(Set<String> notes);

}
