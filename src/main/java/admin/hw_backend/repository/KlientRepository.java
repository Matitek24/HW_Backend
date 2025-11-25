package admin.hw_backend.repository;

import admin.hw_backend.entity.Klient;
import admin.hw_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KlientRepository extends JpaRepository<Klient, Long> {
    Optional<Klient> findByEmail(String email);
}
