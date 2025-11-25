package admin.hw_backend.repository;

import admin.hw_backend.entity.Wzor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WzorRepository extends JpaRepository<Wzor, Long> {
}
