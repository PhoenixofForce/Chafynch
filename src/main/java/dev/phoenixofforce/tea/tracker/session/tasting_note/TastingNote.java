package dev.phoenixofforce.tea.tracker.session.tasting_note;

import org.hibernate.proxy.HibernateProxy;

import lombok.*;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tasting_note")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TastingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;

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
        TastingNote tastingNote = (TastingNote) o;
        return Objects.equals(getId(), tastingNote.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }

}
