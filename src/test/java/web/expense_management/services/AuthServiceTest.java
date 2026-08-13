package web.expense_management.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.expense_management.dtos.AuthResponse;
import web.expense_management.dtos.LoginRequest;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;
import web.expense_management.security.JwtUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setUsername("test@gmail.com");
        mockUser.setPassword("encodedPassword123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("test@gmail.com");
        loginRequest.setPassword("rawPassword123");
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(any(User.class))).thenReturn("mockJwtToken");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        assertEquals("test@gmail.com", response.getUser().getUsername());
        
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("Tài khoản không tồn tại", exception.getMessage());
    }

    @Test
    void testLogin_WrongPassword() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("Mật khẩu không chính xác", exception.getMessage());
    }
}
