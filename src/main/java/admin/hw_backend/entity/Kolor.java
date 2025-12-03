package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "slownik_kolor")
@Getter
@Setter
public class Kolor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kolor")
    private Long id;

    @Column(nullable = false)
    private String nazwa;

    @Column(nullable = false, length = 7)
    private String hex;


}