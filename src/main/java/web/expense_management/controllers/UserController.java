package web.expense_management.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.ProfileUpdateRequest;
import web.expense_management.models.User;
import web.expense_management.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        log.info("Cập nhật profile user");
        User user = userService.updateProfile(request);
        return ResponseEntity.ok(Map.of("user", user));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        log.info("Lấy thông tin profile user");
        User user = userService.getProfile();
        return ResponseEntity.ok(Map.of("user", user));
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestBody Map<String, String> body) {
        log.info("Cập nhật avatar user");
        Map<String, String> response = userService.uploadAvatar(body.get("avatarUrl"));
        return ResponseEntity.ok(response);
    }
}