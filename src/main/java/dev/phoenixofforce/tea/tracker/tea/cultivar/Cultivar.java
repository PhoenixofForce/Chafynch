package dev.phoenixofforce.tea.tracker.tea.cultivar;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cultivar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cultivar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}