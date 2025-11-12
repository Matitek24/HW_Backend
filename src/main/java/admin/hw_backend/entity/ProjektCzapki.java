package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;


@Entity
@Table(name = "projekt_czapki")
@Getter
@Setter
public class ProjektCzapki {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projekt")
    private Long id;

    @Column(name = "nazwa_projektu", nullable = false)
    private String nazwaProjektu;

    @Column(name = "wizualizacja")
    private String wizualizacja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uzytkownik", nullable = false)
    private User uzytkownik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_klient", nullable = false)
    private Klient klient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_typu", nullable = false)
    private TypCzapki typCzapki;

    @OneToOne(mappedBy = "projekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private CzapkaDol dol;

    @OneToOne(mappedBy = "projekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private CzapkaGora gora;

    @OneToOne(mappedBy = "projekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private CzapkaSrodek srodek;

    @OneToOne(mappedBy = "projekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pompon pompon;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
