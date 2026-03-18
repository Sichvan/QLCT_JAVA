package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.AuthResponse;
import web.expense_management.dtos.LoginRequest;
import web.expense_management.dtos.RegisterRequest;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;
import web.expense_management.security.JwtUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils; // Gọi công cụ sinh Token vào đây

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng nhập đầy đủ thông tin"));
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email này đã được sử dụng"));
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername().toLowerCase().trim());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getFullName().trim());
        newUser.setPhone(request.getPhone());
        newUser.setRole("user");
        newUser.setCreatedAt(LocalDateTime.now());

        userRepository.save(newUser);

        // Sinh Token ngay sau khi lưu user
        String token = jwtUtils.generateToken(newUser);

        // Trả về đúng chuẩn App Flutter cần
        return ResponseEntity.status(201).body(new AuthResponse(newUser, token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername().toLowerCase().trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Đăng nhập đúng pass -> Sinh Token
                String token = jwtUtils.generateToken(user);
                
                // Trả về đúng chuẩn
                return ResponseEntity.ok(new AuthResponse(user, token));
            }
        }
        
        return ResponseEntity.status(401).body(Map.of("message", "Email hoặc mật khẩu không chính xác"));
    }
}