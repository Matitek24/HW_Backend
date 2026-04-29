package admin.hw_backend.controller;

import admin.hw_backend.entity.Projekt;
import admin.hw_backend.repository.ProjektRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProjektRepository projektRepository;

    @GetMapping("/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Projekt>> getAllProjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchType,
            Pageable pageable
    ) {
       Page<Projekt> page;
        if (search == null || search.trim().isEmpty()) {
            page = projektRepository.findAll(pageable);
        } else {
            String type = searchType != null ? searchType : "";
            switch (type) {
                case "name":
                    page = projektRepository.findByKlientImieNazwiskoContainingIgnoreCase(search, pageable);
                    break;
                case "email":
                    page = projektRepository.findByKlientEmailContainingIgnoreCase(search, pageable);
                    break;
                case "status":
                    page = projektRepository.findByStatusContainingIgnoreCase(search, pageable);
                    break;
                default:
                    page = projektRepository.findAll(pageable);
            }
        }
        return ResponseEntity.ok(page);
    }


    @PatchMapping("/projects/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProjectStatus(@PathVariable UUID id, @RequestBody String newStatus) {
        String statusClean = newStatus.replace("\"", "");

        Projekt projekt = projektRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projekt nie istnieje"));

        projekt.setStatus(statusClean);
        projektRepository.save(projekt);

        return ResponseEntity.ok().build();
    }
}