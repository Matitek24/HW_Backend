package admin.hw_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiService {

    private final DictionaryService dictionaryService;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    // Metoda główna wywoływana z kontrolera
    public String generateConfigFromText(String userPrompt) {

        // 1. Pobieramy dane ze słowników
        String availableColors = dictionaryService.getAvailableColorsForAi();
        String availableFonts = dictionaryService.getAvailableFontsForAi();
        String availableTopPatterns = dictionaryService.getAvailableTopPatternsForAi();
        String availableBottomPatterns = dictionaryService.getAvailableBottomPatternsForAi();

        // 2. Mocarne instrukcje dla AI
        String systemInstruction = String.format("""
            Jesteś ekspertem-konfiguratorem i głównym designerem czapek zimowych M38.
            Twoim zadaniem jest zamiana opisu klienta na idealnie zbalansowany, estetyczny plik JSON.

            DOSTĘPNE KOLORY (Używaj TYLKO kodów HEX z tej listy!):
            [%s]

            DOSTĘPNE CZCIONKI:
            [%s]
            
            DOSTĘPNE WZORY GÓRNE (ID wzoru - Opis):
            [%s]
            
            DOSTĘPNE WZORY DOLNE (ID wzoru - Opis):
            [%s]

            ANATOMIA CZAPKI:
            - `base.top`: Czubek czapki.
            - `base.middle`: Środkowy pas, na którym znajduje się tekst.
            - `base.bottom`: Wywinięcie/ściągacz na samym dole.
            - `pattern.main`: Kolor wzoru dolnego (musi mocno kontrastować z base.middle i base.bottom).
            - `pattern.top`: Kolor wzoru górnego (musi mocno kontrastować z base.top).
            - `pompons.p1` do `p4`: Frędzle/pompony na samej górze czapki.

            STRUKTURA JSON (ZWRÓĆ DOKŁADNIE TEN FORMAT):
            {
              "customLogo": "",
              "text": { "content": "TEKST", "color": "#HEX", "font": "CZCIONKA", "fontSize": 112, "offsetY": 0 },
              "base": { "top": "#HEX", "middle": "#HEX", "bottom": "#HEX" },
              "pattern": { "main": "#HEX", "top": "#HEX" },
              "pompons": { "show": true, "p1": "#HEX", "p2": "#HEX", "p3": "#HEX", "p4": "#HEX" },
              "patterns": { "top": WSTAW_ID_NUMERYCZNE, "bottom": WSTAW_ID_NUMERYCZNE }
            }

            ZASADY KRYTYCZNE I ESTETYCZNE:
            1. KONTRAST TO ŚWIĘTOŚĆ! Nigdy nie zlewaj wzoru z tłem. Jeśli `base.middle` jest ciemne, `pattern.main` i `text.color` MUSZĄ być jasne (np. białe, żółte).
            2. DOBÓR WZORÓW: 
               - W `patterns.top` wpisz TYLKO ID numeryczne z sekcji "WZORY GÓRNE".
               - W `patterns.bottom` wpisz TYLKO ID numeryczne z sekcji "WZORY DOLNE".
               - Dopasuj nazwę wzoru do klimatu z opisu (np. płatki śniegu na zimę, agresywne linie na sportowo).
            3. HARMONIA POMPONÓW: Nie rób tęczy z pomponów (chyba że klient o to prosi). Używaj 1 lub 2 kolorów na przemian. Najlepiej jeśli `p1` i `p3` są takie same, a `p2` i `p4` takie same. Kolory pomponów powinny nawiązywać do reszty czapki.
            4. DOMYŚLNA KREATYWNOŚĆ: Jeśli klient napisze mało (np. "zrób zieloną czapkę"), dobierz odcienie zieleni, pasujące wzory i stwórz piękną, spójną kompozycję samodzielnie. Jeśli nie poda tekstu, zostaw pusty string w `text.content`.
            """, availableColors, availableFonts, availableTopPatterns, availableBottomPatterns);

        return callGeminiApi(systemInstruction, userPrompt);
    }

    // Wewnętrzna metoda do obsługi żądania HTTP
    private String callGeminiApi(String systemInstruction, String userPrompt) {
        // Używamy modelu gemini-2.0-flash (jest superszybki i tani)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Uciekanie znaków nowej linii i cudzysłowów dla JSON-a
        String safeSystemInstruction = systemInstruction.replace("\"", "\\\"").replace("\n", "\\n");
        String safeUserPrompt = userPrompt.replace("\"", "\\\"").replace("\n", "\\n");

        // Budowanie Ciała Zapytania (z wymuszeniem formatu JSON!)
        String requestBody = """
            {
              "systemInstruction": {
                "parts": [{ "text": "%s" }]
              },
              "contents": [{
                "parts": [{ "text": "%s" }]
              }],
              "generationConfig": {
                "responseMimeType": "application/json"
              }
            }
            """.formatted(safeSystemInstruction, safeUserPrompt);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            return extractTextFromGeminiResponse(response.getBody());
        } catch (Exception e) {
            System.err.println("Błąd API Gemini: " + e.getMessage());
            throw new RuntimeException("Nie udało się połączyć z AI.");
        }
    }

    // Wyciąganie samego czystego JSONa z odpowiedzi Google
    private String extractTextFromGeminiResponse(String responseBody) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(responseBody);
            return rootNode.path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();
        } catch (Exception e) {
            return "{}"; // w razie błędu parsera zwracamy pusty obiekt
        }
    }
}