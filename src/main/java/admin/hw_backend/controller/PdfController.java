package admin.hw_backend.controller;

import admin.hw_backend.service.EmailService;
import admin.hw_backend.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.IOException;

@RestController
@RequestMapping("/api/public/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final EmailService emailService;
    private final RateLimitingService rateLimitingService;

    @PostMapping("/send-pdf")
    public ResponseEntity<?> handlePdfSend(
            @RequestParam("email") String email,
            @RequestParam(value = "projectId", required = false) UUID projectId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request)
            {

                Bucket bucket = rateLimitingService.resolveBucket(getClientIP(request));
                if (!bucket.tryConsume(1)) {
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Przekroczony limit pobrań");
                }

                String validationError = validateRequest(email, file);
                if (validationError != null) {
                    return ResponseEntity.badRequest().body(validationError);
                }

                try {
                    emailService.sendPdfVisualization(email, file.getBytes(), file.getOriginalFilename(), projectId);
                    return ResponseEntity.ok().build();
                } catch (IOException e) {
                    return ResponseEntity.internalServerError().body("Błąd odczytu pliku");
                }
    }

    // Walidacja czy jest to pdf aby uniknac  zbednych problemow z SQL Injection i Man In the middle
    private String validateRequest(String email, MultipartFile file) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            return "Niepoprawny format adresu e-mail";

        if (file == null || file.isEmpty())
            return "Plik jest pusty";

        if (!"application/pdf".equals(file.getContentType()))
            return "Niedozwolony format pliku. Wymagany PDF.";

        if (file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".pdf"))
            return "Plik musi mieć rozszerzenie .pdf";

        return null;
    }
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}