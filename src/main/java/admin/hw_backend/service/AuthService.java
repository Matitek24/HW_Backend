package admin.hw_backend.service;

import admin.hw_backend.dto.RegisterRequest;
import admin.hw_backend.entity.Role;
import admin.hw_backend.entity.User;
import admin.hw_backend.repository.RoleRepository;
import admin.hw_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
    // Metoda do rejestracji
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email jest już zajęty!");
        }

        // 1. Stwórz nowego użytkownika
        User user = new User();
        user.setEmail(request.getEmail());

        // 2. HASZOWANIE HASŁA!
        user.setHaslo(passwordEncoder.encode(request.getHaslo()));

        // 3. Przypisz domyślną rolę (np. USER)
        Role userRole = roleRepository.findByNazwaRoli("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Nie znaleziono roli 'ROLE_USER'"));

        user.getRole().add(userRole);

        // 4. Zapisz w bazie
        return userRepository.save(user);
    }

}