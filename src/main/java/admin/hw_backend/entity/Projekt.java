package admin.hw_backend.entity;

import admin.hw_backend.model.json.HatConfiguration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "projekt")
@Getter
@Setter
public class Projekt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_projekt")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_klient", nullable = false)
    private Klient klient;

    // --- KLUCZOWE: Mapowanie JSON ---
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "konfiguracja_json", columnDefinition = "jsonb")
    private HatConfiguration konfiguracja;

    @Column(nullable = false)
    private String status;

    @Column(name = "miniaturka_url")
    private String miniaturkaUrl;

    @Column(name = "token_edycji", nullable = false)
    private String tokenEdycji;

    @Column(name = "ilosc_sztuk")
    private Integer iloscSztuk;

    @Column(name = "uwagi_klienta", columnDefinition = "TEXT")
    private String uwagiKlienta;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}