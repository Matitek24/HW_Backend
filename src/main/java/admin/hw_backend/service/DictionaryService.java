package admin.hw_backend.service;

import admin.hw_backend.repository.CzcionkaRepository;
import admin.hw_backend.repository.KolorRepository;
import admin.hw_backend.repository.WzorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final KolorRepository kolorRepository;
    private final CzcionkaRepository czcionkaRepository;
    private final WzorRepository wzorRepository;

    // Zwraca mapowanie dla AI: "Biały: #FFFFFF, Czarny: #000000, Granatowy: #01426A"
    public String getAvailableColorsForAi() {
        return kolorRepository.findAll().stream()
                .map(kolor -> kolor.getNazwa() + ": " + kolor.getHex())
                .collect(Collectors.joining(", "));
    }

    // Zwraca czcionki: "impact, roboto, arial"
    public String getAvailableFontsForAi() {
        return czcionkaRepository.findAll().stream()
                .map(czcionka -> czcionka.getWartosc())
                .collect(Collectors.joining(", "));
    }

    // Zwraca dostępne ID wzorów: "1, 2, 3, 17, 32"
    // Zwraca tylko wzory na górę czapki (np. "17 - Paski, 18 - Kropki")
    public String getAvailableTopPatternsForAi() {
        return wzorRepository.findAll().stream()
                .filter(wzor -> "TOP".equalsIgnoreCase(wzor.getKategoria()))
                .map(wzor -> wzor.getId() + " (" + wzor.getNazwa() + ")")
                .collect(Collectors.joining(", "));
    }

    // Zwraca tylko wzory na dół czapki
    public String getAvailableBottomPatternsForAi() {
        return wzorRepository.findAll().stream()
                .filter(wzor -> "BOTTOM".equalsIgnoreCase(wzor.getKategoria()))
                .map(wzor -> wzor.getId() + " (" + wzor.getNazwa() + ")")
                .collect(Collectors.joining(", "));
    }
}