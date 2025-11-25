package admin.hw_backend.dto;

import admin.hw_backend.model.json.HatConfiguration;
import lombok.Data;

@Data
public class ProjectRequest {
    private String email;
    private String imieNazwisko;
    private String firma;
    private String telefon;
    private String uwagi;
    private Integer ilosc;
    private boolean zgodaRodo;
    private HatConfiguration config;
}