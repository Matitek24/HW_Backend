package admin.hw_backend.service;

import admin.hw_backend.dto.ProjectRequest;
import admin.hw_backend.entity.Klient;
import admin.hw_backend.entity.Projekt;
import admin.hw_backend.repository.KlientRepository;
import admin.hw_backend.repository.ProjektRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final KlientRepository klientRepository;
    private final ProjektRepository projektRepository;

    @Transactional
    public UUID createProject(ProjectRequest request) {
        // 1. Obsługa Klienta (Znajdź istniejącego po mailu lub stwórz nowego)
        Klient klient = klientRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    Klient k = new Klient();
                    k.setEmail(request.getEmail());
                    return k;
                });

        // Aktualizujemy dane klienta (zawsze najświeższe z formularza)
        klient.setImieNazwisko(request.getImieNazwisko());
        klient.setFirma(request.getFirma());
        klient.setTelefon(request.getTelefon());
        klient.setZgodaRodo(request.isZgodaRodo());

        // Zapisujemy/Aktualizujemy klienta
        klient = klientRepository.save(klient);

        // 2. Tworzenie Projektu
        Projekt projekt = new Projekt();
        projekt.setKlient(klient);
        projekt.setKonfiguracja(request.getConfig()); // Tu wpada JSON!
        projekt.setStatus("NOWY");
        projekt.setIloscSztuk(request.getIlosc());
        projekt.setUwagiKlienta(request.getUwagi());

        // Generujemy token do edycji (na przyszłość)
        projekt.setTokenEdycji(UUID.randomUUID().toString());

        projekt = projektRepository.save(projekt);

        return projekt.getId();
    }
}