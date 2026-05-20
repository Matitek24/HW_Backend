package admin.hw_backend.controller;

import admin.hw_backend.dto.ProjectPublicResponse;
import admin.hw_backend.dto.ProjectRequest;
import admin.hw_backend.repository.ProjektRepository;
import admin.hw_backend.service.ProjectService;
import admin.hw_backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjektRepository projektRepository;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitProject(
            @RequestPart("data") ProjectRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            String s3Url = s3Service.uploadFile(file);


            if (request.getConfig() != null) {
                request.getConfig().getLogo().setUrl(s3Url);
            }
        }

        UUID projectId = projectService.createProject(request);
        return ResponseEntity.ok(Map.of(
                "message", "Projekt wysłany pomyślnie!",
                "projectId", projectId
        ));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProjectPublicResponse> getProjectConfig(@PathVariable UUID uuid) {
        return projektRepository.findById(uuid)
                .map(projekt -> new ProjectPublicResponse(
                        projekt.getStatus(),
                        projekt.getKonfiguracja()
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt nie istnieje"));
    }

    @PutMapping(value = "/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProject(
            @PathVariable UUID uuid,
            @RequestPart("data") ProjectRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {

            projektRepository.findById(uuid).ifPresent(oldProjekt -> {
                if(oldProjekt.getKonfiguracja() != null && oldProjekt.getKonfiguracja().getLogo() != null) {
                    String oldUrl = oldProjekt.getKonfiguracja().getLogo().getUrl();
                    if(oldUrl != null){
                        s3Service.deleteFile(oldUrl);
                    }
                }
            });

            String s3Url = s3Service.uploadFile(file);
            if (request.getConfig() != null && request.getConfig().getLogo() != null) {
                request.getConfig().getLogo().setUrl(s3Url);
            }
        }

        projectService.updateProject(uuid, request);
        return ResponseEntity.ok(Map.of("message", "Projekt zaktualizowany pomyślnie!"));
    }
}