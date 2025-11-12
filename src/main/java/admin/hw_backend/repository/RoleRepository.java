package admin.hw_backend.repository;

import admin.hw_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNazwaRoli(String nazwaRoli);
}
