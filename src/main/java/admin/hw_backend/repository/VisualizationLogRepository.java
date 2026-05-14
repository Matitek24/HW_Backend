package admin.hw_backend.repository;

import admin.hw_backend.dto.DailyStatDto;
import admin.hw_backend.entity.VisualizationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisualizationLogRepository extends JpaRepository<VisualizationLog, Long> {
    @Query("SELECT new admin.hw_backend.dto.DailyStatDto(CAST(v.downloadedAt AS localdate), COUNT(v)) " +
            "FROM VisualizationLog v " +
            "GROUP BY CAST(v.downloadedAt AS localdate) " +
            "ORDER BY CAST(v.downloadedAt AS localdate) ASC")
    List<DailyStatDto> getDailyStats();
}