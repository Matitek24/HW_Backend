package admin.hw_backend.repository;

import admin.hw_backend.entity.Projekt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjektRepository extends JpaRepository<Projekt, Long> {
    List<Projekt> findAllByOrderByCreatedAtDesc();
}
