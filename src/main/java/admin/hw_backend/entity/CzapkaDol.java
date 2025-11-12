package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "czapka_dol")
@Getter
@Setter
public class CzapkaDol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dol")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projekt", nullable = false, unique = true)
    private ProjektCzapki projekt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kolor", nullable = false)
    private Kolor kolor;

    @Column(name = "czy_logotyp", nullable = false)
    private boolean czyLogotyp = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
