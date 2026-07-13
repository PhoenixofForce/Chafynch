package dev.phoenixofforce.tea.tracker.tea.cultivar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CultivarRepository extends JpaRepository<Cultivar, Long> {
    Optional<Cultivar> findByName(String name);
    List<Cultivar> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}