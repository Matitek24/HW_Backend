package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="uzytkownik")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uzytkownik")
    private Long id;

    @Column(name = "email", length = 128, nullable = false, unique = true)
    private String email;

    @Column(name = "haslo_hash", nullable = false)
    private String haslo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry_date")
    private OffsetDateTime resetTokenExpiryDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "uzytkownik_rola",
            joinColumns = @JoinColumn(name = "id_uzytkownik"),
            inverseJoinColumns = @JoinColumn(name = "id_rola")
    )

    private Set<Role> role = new HashSet<>();

}
