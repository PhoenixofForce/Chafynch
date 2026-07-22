package dev.phoenixofforce.tea.tracker.tea;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeaRepository extends JpaRepository<Tea, Long> {

    @Override
    @EntityGraph(attributePaths = { "cultivar", "vendor", "teaType", "originLocation" })
    List<Tea> findAll();

    @Override
    @EntityGraph(attributePaths = { "cultivar", "vendor", "teaType", "originLocation" })
    Optional<Tea> findById(Long id);
}
