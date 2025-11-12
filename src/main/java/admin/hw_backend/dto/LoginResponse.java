package admin.hw_backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String typTokenu = "Bearer"; // Typ tokenu, standard JWT

    private String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}