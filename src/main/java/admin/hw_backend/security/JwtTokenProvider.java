package admin.hw_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.Jwts.parser;


import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    // Token musi być tajny (Secret Key)
    // Zostanie on pobrany z pliku application.properties
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    // Czas życia tokenu (np. 7 dni)
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // --- 1. Generowanie Tokenu ---
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // Dodajemy role użytkownika do claims
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String token = Jwts.builder()
                .setSubject(username) // Zazwyczaj email użytkownika
                .claim("roles", roles) // Dodanie ról jako niestandardowy claim
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key(), SignatureAlgorithm.HS512) // Podpisanie kluczem
                .compact();

        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // --- 2. Pobieranie Emaila z Tokenu ---
    public String getUsernameFromToken(String token) {
        Claims claims = parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // --- 3. Walidacja Tokenu ---
    public boolean validateToken(String token) {
        try {
            parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT jest nieprawidłowy lub wygasł", ex);
        }
    }
}