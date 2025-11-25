package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "klient")
@Getter
@Setter
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_klient")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "imie_nazwisko")
    private String imieNazwisko;

    private String firma;
    private String telefon;
    private String uwagi;

    @Column(name = "zgoda_rodo")
    private boolean zgodaRodo;

    @OneToMany(mappedBy = "klient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Projekt> projekty = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}