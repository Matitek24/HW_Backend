package admin.hw_backend.repository;

import admin.hw_backend.entity.Czcionka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CzcionkaRepository extends JpaRepository<Czcionka, Long> {
}
