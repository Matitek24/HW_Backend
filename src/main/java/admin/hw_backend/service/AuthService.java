package admin.hw_backend.service;

import admin.hw_backend.dto.RegisterRequest;
import admin.hw_backend.entity.Role;
import admin.hw_backend.entity.User;
import admin.hw_backend.repository.RoleRepository;
import admin.hw_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email jest już zajęty!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setHaslo(passwordEncoder.encode(request.getHaslo()));

        // Przypisz rolę
        String roleName = determineRoleName(request.getRola());
        Role role = roleRepository.findByNazwaRoli(roleName)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono roli: " + roleName));

        user.getRole().add(role);

        User savedUser = userRepository.save(user);
        log.info("Zarejestrowano użytkownika {} z rolą {}", savedUser.getEmail(), roleName);

        return savedUser;
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o takim emailu"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiryDate(OffsetDateTime.now().plusMinutes(15));

        userRepository.save(user);

        emailService.sendPasswordResetLink(email, token);

    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy token"));

        if (user.getResetTokenExpiryDate().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("Token wygasł!");
        }

        user.setHaslo(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiryDate(null);

        userRepository.save(user);
    }

    private String determineRoleName(String requestedRole) {
        if (requestedRole == null || requestedRole.isBlank()) {
            return "ROLE_USER"; // Domyślna rola
        }

        String normalized = requestedRole.toUpperCase().trim();

        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }

        // Walidacja dozwolonych ról (tylko USER i ADMIN)
        if (normalized.equals("ROLE_USER") || normalized.equals("ROLE_ADMIN")) {
            return normalized;
        }

        throw new RuntimeException("Nieprawidłowa rola: " + requestedRole);
    }

    @Transactional
    public User registerSprzedawca(RegisterRequest request) {
        request.setRola("ROLE_USER");
        return registerUser(request);
    }
}