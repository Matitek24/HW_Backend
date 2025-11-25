package admin.hw_backend.controller;

import admin.hw_backend.dto.LoginRequest;
import admin.hw_backend.dto.LoginResponse;
import admin.hw_backend.dto.RegisterRequest;
import admin.hw_backend.security.JwtTokenProvider;
import admin.hw_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            request.setRola(null);
            authService.registerUser(request);
            return new ResponseEntity<>("Użytkownik zarejestrowany pomyślnie!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getHaslo()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(token, loginRequest.getEmail()));
    }

    @PostMapping("/register-sprzedawca")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerSprzedawca(@RequestBody RegisterRequest request) {
        try {
            authService.registerSprzedawca(request);
            return new ResponseEntity<>(
                    "Sprzedawca " + request.getEmail() + " zarejestrowany pomyślnie!",
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
        try {
            request.setRola("ROLE_ADMIN");
            authService.registerUser(request);
            return new ResponseEntity<>("Admin zarejestrowany pomyślnie!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            authService.forgotPassword(email);
            return ResponseEntity.ok("Link resetujący został wysłany (sprawdź konsolę serwera!)");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestBody String newPassword) {
        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Hasło zostało zmienione pomyślnie.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}