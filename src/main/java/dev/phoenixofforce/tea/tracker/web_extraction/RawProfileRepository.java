package dev.phoenixofforce.tea.tracker.web_extraction;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawProfileRepository extends JpaRepository<RawProfile, Long> {

    @Override
    @EntityGraph(attributePaths = { "settings" })
    List<RawProfile> findAll();

}
