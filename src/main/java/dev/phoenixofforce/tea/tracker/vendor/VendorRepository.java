package dev.phoenixofforce.tea.tracker.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByName(String name);
    List<Vendor> findByNameContainingIgnoreCase(String name);

    @Query("""
    SELECT v as vendor, count(t) as teaCount, COALESCE(AVG(t.price / t.weightGrams), 0) as averagePricePerGram
        FROM Vendor v LEFT JOIN Tea t on t.vendor = v
        WHERE :query IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%'))
        GROUP BY v
    """)
    List<VendorOverview> findVendorOverviews(@Param("query") String query);


}