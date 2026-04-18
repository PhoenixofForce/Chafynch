package dev.phoenixofforce.tea.tracker.tea;

import jakarta.persistence.*;
import lombok.*;

import dev.phoenixofforce.tea.tracker.location.Location;
import dev.phoenixofforce.tea.tracker.tea.cultivar.Cultivar;
import dev.phoenixofforce.tea.tracker.tea.type.TeaType;
import dev.phoenixofforce.tea.tracker.vendor.Vendor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tea")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cultivar_id")
    @ToString.Exclude
    private Cultivar cultivar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tea_type_id")
    @ToString.Exclude
    private TeaType teaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_location_id")
    @ToString.Exclude
    private Location originLocation;

    private BigDecimal price;

    private LocalDate purchaseDate;

    private BigDecimal weightGrams;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Tea tea = (Tea) o;
        return getId() != null && Objects.equals(getId(), tea.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}