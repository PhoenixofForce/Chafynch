package dev.phoenixofforce.tea.tracker.vendor;

import dev.phoenixofforce.tea.tracker.location.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String website;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}