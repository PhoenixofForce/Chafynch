package dev.phoenixofforce.tea.tracker.tea.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeaTypeRepository extends JpaRepository<TeaType, Long> {

    Optional<TeaType> findByName(String name);
}