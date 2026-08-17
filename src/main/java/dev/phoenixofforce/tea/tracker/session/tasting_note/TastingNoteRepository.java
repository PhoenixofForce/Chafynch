package dev.phoenixofforce.tea.tracker.session.tasting_note;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TastingNoteRepository extends JpaRepository<TastingNote, Long> {

    List<TastingNote> findAllByNoteIn(Set<String> notes);

    @Query("""
            Select t.note
            from TastingNote t
            where lower(t.note) like lower(concat('%', :query, '%'))
            order by t.note
        """)
    List<String> findNotes(String query, Limit limit);

}
