package admin.hw_backend.controller;

import admin.hw_backend.entity.Projekt;
import admin.hw_backend.repository.ProjektRepository;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Projekt>> getAllProjects() {
        return ResponseEntity.ok(projektRepository.findAllWithKlient());
    }

    // w AdminController

    @PatchMapping("/projects/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProjectStatus(@PathVariable UUID id, @RequestBody String newStatus) {
        // newStatus może przyjść w cudzysłowach (np. "W_REALIZACJI"), więc warto go oczyścić
        String statusClean = newStatus.replace("\"", "");

        Projekt projekt = projektRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projekt nie istnieje"));

        projekt.setStatus(statusClean);
        projektRepository.save(projekt);

        return ResponseEntity.ok().build();
    }
}