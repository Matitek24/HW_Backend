package admin.hw_backend;

import admin.hw_backend.dto.RegisterRequest;
import admin.hw_backend.entity.Role;
import admin.hw_backend.entity.User;
import admin.hw_backend.repository.RoleRepository;
import admin.hw_backend.repository.UserRepository;
import admin.hw_backend.service.AuthService;
import admin.hw_backend.service.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setHaslo("password123");
        registerRequest.setRola("USER");
    }

    @Test
    void registerUser_ShouldSaveUser_WhenDataIsValid() {
        // GIVEN
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        Role role = new Role();
        role.setNazwaRoli("ROLE_USER");

        when(roleRepository.findByNazwaRoli("ROLE_USER"))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        // WHEN
        User savedUser = authService.registerUser(registerRequest);

        // THEN
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("hashed_password", savedUser.getHaslo());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // GIVEN
        when(userRepository.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        // WHEN & THEN
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.registerUser(registerRequest)
        );

        assertEquals("Email jest już zajęty!", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void forgotPassword_ShouldSetTokenAndSendEmail() {
        // GIVEN
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        // WHEN
        authService.forgotPassword("test@example.com");

        // THEN
        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiryDate());

        verify(userRepository).save(user);
        verify(emailService)
                .sendPasswordResetLink(eq("test@example.com"), anyString());
    }
}
