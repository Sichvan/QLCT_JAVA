package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.AuthResponse;
import web.expense_management.dtos.LoginRequest;
import web.expense_management.dtos.RegisterRequest;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;
import web.expense_management.security.JwtUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse login(LoginRequest request) {
        log.info("Processing login for user: {}", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for user: {}", request.getUsername());
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        String token = jwtUtils.generateToken(user);
        return new AuthResponse(user, token);
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("Processing registration for user: {}", request.getUsername());
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtUtils.generateToken(user);
        return new AuthResponse(user, token);
    }
}
