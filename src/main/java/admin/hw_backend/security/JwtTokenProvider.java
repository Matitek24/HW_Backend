package admin.hw_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.Jwts.parser;


import java.security.Key;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

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
        } catch (MalformedJwtException ex) {
            log.error("Nieprawidłowy token JWT: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT wygasł: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT nie jest wspierany: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string jest pusty: {}", ex.getMessage());
        }
        return false;
    }
}