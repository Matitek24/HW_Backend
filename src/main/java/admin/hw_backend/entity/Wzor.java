package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "slownik_wzor")
@Getter
@Setter
public class Wzor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wzor")
    private Long id;

    @Column(nullable = false)
    private String nazwa;

    @Lob // Informuje Hibernate, że to jest duży tekst (CLOB/TEXT)
    @Column(name = "kod_svg", nullable = false, columnDefinition = "TEXT")
    private String kodSvg;

    @Column(nullable = false)
    private String kategoria; // Wartości: 'GORA', 'DOL', 'SRODEK' (wg Twojego ERD i specyfikacji)
}