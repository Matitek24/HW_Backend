package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "pompon")
@Getter
@Setter
public class Pompon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pompon")
    private Long id;

    @Column(name = "liczba_nici", nullable = false)
    private Integer liczbaNici;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projekt", nullable = false, unique = true)
    private ProjektCzapki projekt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kolor", nullable = false)
    private Kolor kolor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
