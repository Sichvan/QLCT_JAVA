package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.ProfileUpdateRequest;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request) {
        User user = userRepository.findById(getCurrentUserId()).orElseThrow();

        // Kiểm tra mật khẩu hiện tại
        if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Mật khẩu hiện tại không chính xác"));
        }

        // Cập nhật thông tin cơ bản
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        // Đổi mật khẩu mới (nếu có)
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (!request.getNewPassword().matches("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")) {
                return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu mới phải có ít nhất 6 ký tự, bao gồm chữ và số"));
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("user", user));
    }
}