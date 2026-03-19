package dev.phoenixofforce.tea.tracker.tea.type;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tea_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeaType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}