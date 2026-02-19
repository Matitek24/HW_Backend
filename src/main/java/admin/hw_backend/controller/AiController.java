package admin.hw_backend.controller;


import admin.hw_backend.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateConfig(@RequestBody Map<String, String> request) {
        try {
            String userText = request.get("prompt");
            String jsonResult = aiService.generateConfigFromText(userText);
            // Zwracamy czystego JSONa
            return ResponseEntity.ok(jsonResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}