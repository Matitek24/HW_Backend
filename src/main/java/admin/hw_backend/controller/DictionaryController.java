package admin.hw_backend.controller;

import admin.hw_backend.entity.Czcionka;
import admin.hw_backend.entity.Kolor;
import admin.hw_backend.entity.Wzor;
import admin.hw_backend.repository.CzcionkaRepository;
import admin.hw_backend.repository.KolorRepository;
import admin.hw_backend.repository.WzorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final KolorRepository kolorRepository;
    private final WzorRepository wzorRepository;
    private final CzcionkaRepository czcionkaRepository;

    @GetMapping("/colors")
    public ResponseEntity<List<Kolor>> getColors() {
        return ResponseEntity.ok(kolorRepository.findAll());
    }

    @GetMapping("/patterns")
    public ResponseEntity<List<Wzor>> getPatterns() {
        return ResponseEntity.ok(wzorRepository.findAll());
    }

    @GetMapping("/fonts")
    public ResponseEntity<List<Czcionka>> getFonts() {
        return ResponseEntity.ok(czcionkaRepository.findAll());
    }
}