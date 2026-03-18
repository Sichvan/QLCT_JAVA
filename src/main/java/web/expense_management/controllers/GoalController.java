package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.DepositRequest;
import web.expense_management.dtos.GoalRequest;
import web.expense_management.models.Goal;
import web.expense_management.models.User;
import web.expense_management.repositories.GoalRepository;
import web.expense_management.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy ID user từ Token
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    // 1. LẤY DANH SÁCH MỤC TIÊU
    @GetMapping
    public ResponseEntity<List<Goal>> getGoals() {
        List<Goal> goals = goalRepository.findByUserOrderByCreatedAtDesc(getCurrentUserId());
        return ResponseEntity.ok(goals);
    }

    // 2. TẠO MỤC TIÊU MỚI
    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalRequest request) {
        Goal goal = new Goal();
        goal.setUser(getCurrentUserId());
        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setDeadline(request.getDeadline());

        Goal savedGoal = goalRepository.save(goal);
        return ResponseEntity.status(201).body(savedGoal);
    }

    // 3. NẠP TIỀN VÀO MỤC TIÊU
    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> depositToGoal(@PathVariable String id, @RequestBody DepositRequest request) {
        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số tiền không hợp lệ"));
        }

        Optional<Goal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty() || !goalOpt.get().getUser().equals(getCurrentUserId())) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy mục tiêu"));
        }

        // Lấy thông tin user hiện tại từ DB để lấy số dư mới nhất
        User user = userRepository.findById(getCurrentUserId()).orElseThrow();
        Goal goal = goalOpt.get();

        if (user.getBalance() < request.getAmount()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số dư ví không đủ để tiết kiệm"));
        }

        // Thực hiện giao dịch
        user.setBalance(user.getBalance() - request.getAmount());
        goal.setCurrentAmount(goal.getCurrentAmount() + request.getAmount());

        boolean isSuccess = false;
        if (goal.getCurrentAmount() >= goal.getTargetAmount()) {
            goal.setStatus("completed");
            isSuccess = true;
        }

        userRepository.save(user);
        goalRepository.save(goal);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Nạp tiền thành công");
        response.put("updatedGoal", goal);
        response.put("newBalance", user.getBalance());
        response.put("isSuccess", isSuccess);

        return ResponseEntity.ok(response);
    }

    // 4. XÓA MỤC TIÊU (Có hoàn tiền)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable String id) {
        Optional<Goal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty() || !goalOpt.get().getUser().equals(getCurrentUserId())) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy mục tiêu"));
        }

        Goal goal = goalOpt.get();
        String message = "Đã xóa mục tiêu thành công";

        // Hoàn tiền nếu mục tiêu chưa xong và đang có tiền
        if ("ongoing".equals(goal.getStatus()) && goal.getCurrentAmount() > 0 && goal.getCurrentAmount() < goal.getTargetAmount()) {
            User user = userRepository.findById(getCurrentUserId()).orElseThrow();
            user.setBalance(user.getBalance() + goal.getCurrentAmount());
            userRepository.save(user);
            message = String.format("Đã xóa. Hoàn lại %,.0f đ vào ví.", goal.getCurrentAmount());
        }

        goalRepository.delete(goal);
        return ResponseEntity.ok(Map.of("message", message));
    }
}