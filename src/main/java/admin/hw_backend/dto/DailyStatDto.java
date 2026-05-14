package admin.hw_backend.dto;

import java.time.LocalDate;

public record DailyStatDto(LocalDate date, Long count) {
}
