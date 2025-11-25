package admin.hw_backend.model.json;

import lombok.Data;
import java.io.Serializable;

@Data
public class HatConfiguration implements Serializable {
    // Odzwierciedla strukturę JSON z frontendu

    private TextConfig text;
    private BaseConfig base;
    private PatternConfig pattern;
    private PomponConfig pompons;
    private PatternsSelection patterns;

    @Data
    public static class TextConfig {
        private String content;
        private String color;
        private String font;
        private Integer fontSize;
    }

    @Data
    public static class BaseConfig {
        private String top;     // hex
        private String middle;  // hex
        private String bottom;  // hex
    }

    @Data
    public static class PatternConfig {
        private String main; // kolor wzoru głównego
        private String top;  // kolor wzoru górnego
    }

    @Data
    public static class PomponConfig {
        private String p1;
        private String p2;
        private String p3;
        private String p4;
    }

    @Data
    public static class PatternsSelection {
        private String top;    // nazwa pliku svg ze słownika
        private String bottom; // nazwa pliku svg ze słownika
    }
}