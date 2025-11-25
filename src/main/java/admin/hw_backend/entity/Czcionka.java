package admin.hw_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "slownik_czcionka")
@Getter
@Setter
public class Czcionka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_czcionka")
    private Long id;

    @Column(nullable = false)
    private String nazwa;

    @Column(nullable = false)
    private String wartosc; // Np. nazwa pliku albo rodzina czcionki w CSS
}