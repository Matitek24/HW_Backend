package admin.hw_backend.controller;

import admin.hw_backend.dto.ProjectRequest;
import admin.hw_backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> submitProject(@RequestBody ProjectRequest request) {
        UUID projectId = projectService.createProject(request);
        return ResponseEntity.ok(Map.of(
                "message", "Projekt wysłany pomyślnie!",
                "projectId", projectId
        ));
    }
}