package admin.hw_backend.service;

import admin.hw_backend.dto.ProjectRequest;
import admin.hw_backend.entity.Klient;
import admin.hw_backend.entity.Projekt;
import admin.hw_backend.repository.KlientRepository;
import admin.hw_backend.repository.ProjektRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final KlientRepository klientRepository;
    private final ProjektRepository projektRepository;

    @Transactional
    public UUID createProject(ProjectRequest request) {
        Klient klient = klientRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    Klient k = new Klient();
                    k.setEmail(request.getEmail());
                    return k;
                });

        klient.setImieNazwisko(request.getImieNazwisko());
        klient.setFirma(request.getFirma());
        klient.setTelefon(request.getTelefon());
        klient.setZgodaRodo(request.isZgodaRodo());

        klient = klientRepository.save(klient);

        Projekt projekt = new Projekt();
        projekt.setKlient(klient);
        projekt.setKonfiguracja(request.getConfig());
        projekt.setStatus("NOWY");
        projekt.setIloscSztuk(request.getIlosc());
        projekt.setUwagiKlienta(request.getUwagi());

        // Generujemy token do edycji (na przyszłość)
        projekt.setTokenEdycji(UUID.randomUUID().toString());

        projekt = projektRepository.save(projekt);

        return projekt.getId();
    }

    @Transactional
    public void updateProject(UUID projectId, ProjectRequest request) {

        Projekt projekt = projektRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt nie istnieje"));

        if (!"NOWY".equalsIgnoreCase(projekt.getStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Edycja zablokowana. Projekt jest w statusie: " + projekt.getStatus());
        }

        projekt.setKonfiguracja(request.getConfig());


        projektRepository.save(projekt);
    }
}