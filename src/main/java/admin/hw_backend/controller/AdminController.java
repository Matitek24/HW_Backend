package admin.hw_backend.controller;

import admin.hw_backend.entity.Projekt;
import admin.hw_backend.repository.ProjektRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProjektRepository projektRepository;

    // Endpoint dostępny tylko dla ADMINA
    @GetMapping("/projects")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Projekt>> getAllProjects() {
        // Pobieramy wszystkie projekty, sortując od najnowszych
        // Warto dodać metodę findAllByOrderByCreatedAtDesc() w repozytorium
        return ResponseEntity.ok(projektRepository.findAllByOrderByCreatedAtDesc());
    }
}