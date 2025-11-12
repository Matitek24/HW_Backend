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

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "czy_aktywny", nullable = false)
    private boolean czyAktywny = true;

    @OneToMany(mappedBy = "klient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjektCzapki> projekty = new ArrayList<>();

    @OneToMany(mappedBy = "klient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zaproszenie> zaproszenia = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
