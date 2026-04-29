package admin.hw_backend.repository;

import admin.hw_backend.entity.Projekt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProjektRepository extends JpaRepository<Projekt, UUID> {

//    @Query("SELECT p FROM Projekt p LEFT JOIN FETCH p.klient ORDER BY p.createdAt DESC")
//    List<Projekt> findAllWithKlient();

    @EntityGraph(attributePaths = {"klient"})
    Page<Projekt> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"klient"})
    Page<Projekt> findByKlientImieNazwiskoContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"klient"})
    Page<Projekt> findByKlientEmailContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"klient"})
    Page<Projekt> findByStatusContainingIgnoreCase(String name, Pageable pageable);


}
