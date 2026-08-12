package dev.phoenixofforce.tea.tracker.session;

import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNote;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "session_tasting_notes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SessionTastingNote {

    @EmbeddedId
    private SessionTastingNoteId id = new SessionTastingNoteId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @MapsId("sessionId")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tasting_note_id", nullable = false)
    @MapsId("tastingNoteId")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TastingNote tastingNote;

    private String category;

    private String subCategory;

    @Embeddable
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionTastingNoteId implements Serializable {

        private Long sessionId;

        private Long tastingNoteId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!(o instanceof SessionTastingNoteId id)) return false;

            return Objects.equals(getSessionId(), id.getSessionId()) &&
                Objects.equals(getTastingNoteId(), id.getTastingNoteId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSessionId(), getTastingNoteId());
        }
    }
}
