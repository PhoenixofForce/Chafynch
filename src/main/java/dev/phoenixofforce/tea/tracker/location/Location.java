package dev.phoenixofforce.tea.tracker.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    private String province;

    private String city;

    @Min(-180)
    @Max(180)
    private Double longitude;

    @Min(-90)
    @Max(90)
    private Double latitude;
}