package admin.hw_backend.repository;

import admin.hw_backend.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    Optional<Lead> findByEmail(String email);
}