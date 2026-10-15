package admin.hw_backend.controller;

import admin.hw_backend.entity.Kolor;
import admin.hw_backend.entity.Wzor;
import admin.hw_backend.repository.KolorRepository;
import admin.hw_backend.repository.WzorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dictionary")
@RequiredArgsConstructor
public class AdminDictionaryController {

    private final KolorRepository kolorRepository;
    private final WzorRepository wzorRepository;

    // ROZWIĄZANIE 1: Zawsze sortujemy po ID rosnąco
    @GetMapping("/colors")
    public ResponseEntity<List<Kolor>> getAllColors() {
        return ResponseEntity.ok(kolorRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @PostMapping("/colors")
    public ResponseEntity<Kolor> addColor(@RequestBody Kolor kolor) {
        if (kolor.getIsActive() == null) kolor.setIsActive(true);
        return ResponseEntity.ok(kolorRepository.save(kolor));
    }

    // ROZWIĄZANIE 2: Import masowy z JSON
    @PostMapping("/colors/bulk")
    public ResponseEntity<List<Kolor>> addColorsBulk(@RequestBody List<Kolor> kolory) {
        kolory.forEach(k -> {
            if (k.getIsActive() == null) k.setIsActive(true);
        });
        return ResponseEntity.ok(kolorRepository.saveAll(kolory));
    }

    @PutMapping("/colors/{id}/status")
    public ResponseEntity<?> updateColorStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        return kolorRepository.findById(id).map(kolor -> {
            kolor.setIsActive(payload.get("isActive"));
            kolorRepository.save(kolor);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/colors/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable Long id) {
        return kolorRepository.findById(id).map(kolor -> {
            try {
                kolorRepository.delete(kolor);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Kolor w użyciu."));
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/colors/bulk")
    public ResponseEntity<?> deleteColorsBulk(@RequestBody List<Long> ids) {
        try {
            kolorRepository.deleteAllById(ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Jeden lub więcej kolorów jest w użyciu."));
        }
    }



//    --------------- patterns

    @GetMapping("/patterns")
    public ResponseEntity<List<Wzor>> getAllPatterns() {
        return ResponseEntity.ok(wzorRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    // 2. Dodaj nowy wzór z wklejonym kodem SVG
    @PostMapping("/patterns")
    public ResponseEntity<Wzor> addPattern(@RequestBody Wzor wzor) {
        if (wzor.getIsActive() == null) {
            wzor.setIsActive(true);
        }
        return ResponseEntity.ok(wzorRepository.save(wzor));
    }

    // 3. Zmień status (Ukryj / Pokaż)
    @PutMapping("/patterns/{id}/status")
    public ResponseEntity<?> updatePatternStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        return wzorRepository.findById(id).map(wzor -> {
            wzor.setIsActive(payload.get("isActive"));
            wzorRepository.save(wzor);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. Usuń wzór (tylko, jeśli żaden stary projekt go nie używa)
    @DeleteMapping("/patterns/{id}")
    public ResponseEntity<?> deletePattern(@PathVariable Long id) {
        return wzorRepository.findById(id).map(wzor -> {
            try {
                wzorRepository.delete(wzor);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Wzór jest przypisany do zapisanego projektu. Spróbuj go po prostu Ukryć."));
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Masowe usuwanie (jeśli chciałbyś dodać checkboxy do widoku DictPatterns)
    @DeleteMapping("/patterns/bulk")
    public ResponseEntity<?> deletePatternsBulk(@RequestBody List<Long> ids) {
        try {
            wzorRepository.deleteAllById(ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Jeden lub więcej wzorów jest w użyciu."));
        }
    }
}

