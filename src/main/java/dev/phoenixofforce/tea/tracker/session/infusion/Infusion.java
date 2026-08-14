package dev.phoenixofforce.tea.tracker.session.infusion;

import dev.phoenixofforce.tea.tracker.session.Session;

import org.hibernate.proxy.HibernateProxy;

import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "infusion")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Infusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startTime;

    private BigDecimal infusionTime;

    private BigDecimal temperature;

    private Integer rating;

    private boolean isRinse;

    @OneToMany(mappedBy = "infusion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InfusionTastingNote> tastingNotes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @ToString.Exclude
    private Session session;

    public void addTastingNote(InfusionTastingNote tastingNote) {
        this.tastingNotes.add(tastingNote);
        tastingNote.setInfusion(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Infusion infusion = (Infusion) o;
        return Objects.equals(getId(), infusion.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }

}
