package dev.phoenixofforce.tea.tracker.vendor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByName(String name);
    List<Vendor> findByNameContainingIgnoreCase(String name);
}