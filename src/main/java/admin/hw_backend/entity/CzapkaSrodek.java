package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "czapka_srodek")
@Getter
@Setter
public class CzapkaSrodek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_srodek")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projekt", nullable = false, unique = true)
    private ProjektCzapki projekt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_wzor", nullable = false)
    private Wzor wzor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kolor", nullable = false)
    private Kolor kolor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_czcionki", nullable = false)
    private Czcionka czcionka;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rozmiaru_czcionki", nullable = false)
    private RozmiarCzcionki rozmiarCzcionki;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kolor_wzoru", nullable = false)
    private Kolor kolorWzoru;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
