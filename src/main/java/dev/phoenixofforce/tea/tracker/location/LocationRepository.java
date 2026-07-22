package dev.phoenixofforce.tea.tracker.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCountryAndProvinceAndCity(String country, String province, String city);

    @Query("SELECT l FROM Location l WHERE LOWER(l.country) LIKE LOWER(CONCAT('%', :q, '%')) " +
        "OR LOWER(l.province) LIKE LOWER(CONCAT('%', :q, '%')) " +
        "OR LOWER(l.city) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Location> search(String q);
}
