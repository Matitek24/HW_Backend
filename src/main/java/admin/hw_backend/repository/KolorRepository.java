package admin.hw_backend.repository;

import admin.hw_backend.entity.Kolor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KolorRepository extends JpaRepository<Kolor, Long> {
}
