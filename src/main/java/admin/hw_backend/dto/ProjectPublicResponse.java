package admin.hw_backend.dto;

import admin.hw_backend.model.json.HatConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectPublicResponse {
    private String status;
    private HatConfiguration config;
}