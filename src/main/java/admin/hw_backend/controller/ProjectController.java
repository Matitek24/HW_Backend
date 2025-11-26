package admin.hw_backend.controller;

import admin.hw_backend.dto.ProjectRequest;
import admin.hw_backend.model.json.HatConfiguration;
import admin.hw_backend.repository.ProjektRepository;
import admin.hw_backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import admin.hw_backend.entity.Projekt;


import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjektRepository projektRepository;

    @PostMapping
    public ResponseEntity<?> submitProject(@RequestBody ProjectRequest request) {
        UUID projectId = projectService.createProject(request);
        return ResponseEntity.ok(Map.of(
                "message", "Projekt wysłany pomyślnie!",
                "projectId", projectId
        ));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<HatConfiguration> getProjectConfig(@PathVariable UUID uuid) {
        return projektRepository.findById(uuid)
                .map(Projekt::getKonfiguracja)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt nie istnieje"));
    }
}