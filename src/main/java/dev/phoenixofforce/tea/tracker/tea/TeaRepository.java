package dev.phoenixofforce.tea.tracker.tea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeaRepository extends JpaRepository<Tea, Long> {

    @Query("SELECT t FROM Tea t LEFT JOIN FETCH t.cultivar LEFT JOIN FETCH t.teaType LEFT JOIN FETCH t.vendor LEFT JOIN FETCH t.originLocation")
    List<Tea> findAllWithRelations();
}