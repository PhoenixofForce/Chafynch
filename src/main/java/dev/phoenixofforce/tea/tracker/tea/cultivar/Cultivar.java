package dev.phoenixofforce.tea.tracker.tea.cultivar;

import org.hibernate.proxy.HibernateProxy;

import lombok.*;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cultivar")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cultivar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

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
        Cultivar cultivar = (Cultivar) o;
        return Objects.equals(getId(), cultivar.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }
}
