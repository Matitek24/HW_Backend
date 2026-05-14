package admin.hw_backend.controller;

import admin.hw_backend.dto.DailyStatDto;
import admin.hw_backend.entity.Lead;
import admin.hw_backend.entity.VisualizationLog;
import admin.hw_backend.repository.LeadRepository;
import admin.hw_backend.repository.VisualizationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {
    private final LeadRepository leadRepository;
    private final VisualizationLogRepository visualizationLogRepository;

    @GetMapping("/leads")
    public List<Lead> getAllLeads(@RequestParam(defaultValue = "lastActivityDate") String sortBy) {
        return leadRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
    }

    @GetMapping("/stats/daily")
    public List<DailyStatDto> getDailystats(){
        return visualizationLogRepository.getDailyStats();
    }

}
