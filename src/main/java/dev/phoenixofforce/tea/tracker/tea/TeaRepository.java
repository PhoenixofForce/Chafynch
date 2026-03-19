package dev.phoenixofforce.tea.tracker.tea;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeaRepository extends JpaRepository<Tea, Long> {
}