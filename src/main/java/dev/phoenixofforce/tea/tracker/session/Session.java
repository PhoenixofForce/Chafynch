package dev.phoenixofforce.tea.tracker.session;

import dev.phoenixofforce.tea.tracker.session.infusion.Infusion;
import dev.phoenixofforce.tea.tracker.tea.Tea;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "session")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Formula("""
        (Select count(*)
        From session s
        Where s.tea_id = tea_id and s.id <= id)
        """)
    private Long sessionNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tea_id", nullable = false)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tea tea;

    private Instant startTime;

    private Instant lastUpdated;

    private BigDecimal weight;

    private BigDecimal volume;

    private String location;

    private String people;

    private Integer rating;

    private String sessionSummary;

    private String nextSessionHint;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SessionTastingNote> tastingNotes = new ArrayList<>();

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("startTime")
    @ToString.Exclude
    private List<Infusion> infusions = new ArrayList<>();

    public void addInfusion(Infusion infusion) {
        this.infusions.add(infusion);
        infusion.setSession(this);
    }

    public void addTastingNote(SessionTastingNote tastingNote) {
        this.tastingNotes.add(tastingNote);
        tastingNote.setSession(this);
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
        Session session = (Session) o;
        return Objects.equals(getId(), session.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }

}
