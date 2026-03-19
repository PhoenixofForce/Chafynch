package dev.phoenixofforce.tea.tracker.tea;

import dev.phoenixofforce.tea.tracker.location.Location;
import dev.phoenixofforce.tea.tracker.tea.cultivar.Cultivar;
import dev.phoenixofforce.tea.tracker.tea.type.TeaType;
import dev.phoenixofforce.tea.tracker.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tea")
@Data
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
    private Cultivar cultivar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tea_type_id")
    private TeaType teaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_location_id")
    private Location originLocation;

    private BigDecimal price;

    private LocalDate purchaseDate;

    private BigDecimal weightGrams;
}