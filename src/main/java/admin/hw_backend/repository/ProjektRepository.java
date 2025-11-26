package admin.hw_backend.repository;

import admin.hw_backend.entity.Projekt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProjektRepository extends JpaRepository<Projekt, UUID> {
    List<Projekt> findAllByOrderByCreatedAtDesc();
    @Query("SELECT p FROM Projekt p LEFT JOIN FETCH p.klient ORDER BY p.createdAt DESC")
    List<Projekt> findAllWithKlient();
}
