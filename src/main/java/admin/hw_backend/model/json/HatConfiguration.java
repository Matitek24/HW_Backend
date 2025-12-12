package admin.hw_backend.model.json;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class HatConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customLogo;
    private TextConfig text;
    private BaseConfig base;
    private PatternConfig pattern;
    private PomponConfig pompons;
    private PatternsSelection patterns;


    @Data
    public static class TextConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        private String content;
        private String color;
        private String font;
        private Integer fontSize;
        private Integer offsetY;
    }

    @Data
    public static class BaseConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        private String top;
        private String middle;
        private String bottom;
    }

    @Data
    public static class PatternConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        private String main;
        private String top;
    }

    @Data
    public static class PomponConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        private Boolean show = true;
        private String p1;
        private String p2;
        private String p3;
        private String p4;
    }

    @Data
    public static class PatternsSelection implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long top;
        private Long bottom;
    }
}