package admin.hw_backend.service;

import admin.hw_backend.entity.Lead;
import admin.hw_backend.entity.VisualizationLog;
import admin.hw_backend.repository.LeadRepository;
import admin.hw_backend.repository.VisualizationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeadTrackerService {
    private final LeadRepository leadRepository;
    private final VisualizationLogRepository visualizationLogRepository;

    public void recordPdfDownload(String email){
        LocalDateTime now = LocalDateTime.now();

        Lead lead = leadRepository.findByEmail(email).orElse(null);

        if(lead != null){
            lead.setDownloadCount(lead.getDownloadCount() + 1);
            lead.setLastActivityDate(now);
            leadRepository.save(lead);
        }
        else{
            Lead newLead = Lead.builder()
                    .email(email)
                    .downloadCount(1)
                    .firstActivityDate(now)
                    .lastActivityDate(now)
                    .build();
            leadRepository.save(newLead);
        }

        VisualizationLog visualizationLog = VisualizationLog.builder()
                .email(email)
                .downloadedAt(now)
                .build();
        visualizationLogRepository.save(visualizationLog);
    }

}
