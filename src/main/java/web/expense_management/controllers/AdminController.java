package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private UserRepository userRepository;

    // Kiểm tra quyền Admin
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return "admin".equals(user.getRole());
    }

    // 1. LẤY THỐNG KÊ DASHBOARD
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        if (!isAdmin()) return ResponseEntity.status(403).body(Map.of("message", "Yêu cầu quyền Admin"));

        List<User> allUsers = userRepository.findAll();
        int totalUsers = 0;
        int newUsers = 0;
        int[] chartData = new int[12]; // Mảng 12 tháng

        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        int currentYear = LocalDateTime.now().getYear();

        for (User u : allUsers) {
            if ("user".equals(u.getRole())) {
                totalUsers++;
                // Đếm user mới
                if (u.getCreatedAt() != null && u.getCreatedAt().isAfter(threeDaysAgo)) {
                    newUsers++;
                }
                // Thống kê biểu đồ theo tháng trong năm nay
                if (u.getCreatedAt() != null && u.getCreatedAt().getYear() == currentYear) {
                    chartData[u.getCreatedAt().getMonthValue() - 1]++;
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", totalUsers);
        response.put("newUsers", newUsers);
        response.put("chartData", chartData);
        return ResponseEntity.ok(response);
    }

    // 2. LẤY DANH SÁCH USER
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        if (!isAdmin()) return ResponseEntity.status(403).body(Map.of("message", "Yêu cầu quyền Admin"));
        
        List<User> users = new ArrayList<>();
        for (User u : userRepository.findAll()) {
            if ("user".equals(u.getRole())) users.add(u);
        }
        // Có thể sort theo thời gian tạo nếu muốn
        return ResponseEntity.ok(users);
    }

    // 3. XÓA NGƯỜI DÙNG
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!isAdmin()) return ResponseEntity.status(403).body(Map.of("message", "Yêu cầu quyền Admin"));

        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy user"));
        if ("admin".equals(user.getRole())) return ResponseEntity.badRequest().body(Map.of("message", "Không thể xóa Admin"));

        userRepository.delete(user);
        // Lưu ý: Đáng lẽ ra phải xóa luôn Expense, Income, Loan của user này nữa, 
        // nhưng mình giữ y hệt logic Node.js của bạn là chỉ xóa account.
        return ResponseEntity.ok(Map.of("message", "Đã xóa người dùng"));
    }
}