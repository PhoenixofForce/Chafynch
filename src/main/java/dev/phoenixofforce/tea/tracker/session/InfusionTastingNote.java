package dev.phoenixofforce.tea.tracker.session;

import dev.phoenixofforce.tea.tracker.session.tasting_note.TastingNote;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "infusion_tasting_notes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InfusionTastingNote {

    @EmbeddedId
    private InfusionTastingNoteId id = new InfusionTastingNoteId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "infusion_id", nullable = false)
    @MapsId("infusionId")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Infusion infusion;

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
    public static class InfusionTastingNoteId implements Serializable {

        private Long infusionId;

        private Long tastingNoteId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!(o instanceof InfusionTastingNoteId id)) return false;

            return Objects.equals(getInfusionId(), id.getInfusionId()) &&
                Objects.equals(getTastingNoteId(), id.getTastingNoteId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getInfusionId(), getTastingNoteId());
        }
    }
}
