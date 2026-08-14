package dev.phoenixofforce.tea.tracker.session.infusion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InfusionRepository extends JpaRepository<Infusion, Long> {

    List<Infusion> findAllByIdIn(Set<Long> ids);

}
