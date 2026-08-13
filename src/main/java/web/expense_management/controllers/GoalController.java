package web.expense_management.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.DepositRequest;
import web.expense_management.dtos.GoalRequest;
import web.expense_management.models.Goal;
import web.expense_management.services.GoalService;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Slf4j
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public ResponseEntity<List<Goal>> getGoals() {
        log.info("Lấy danh sách mục tiêu");
        return ResponseEntity.ok(goalService.getGoals());
    }

    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalRequest request) {
        log.info("Tạo mục tiêu mới: {}", request.getName());
        return ResponseEntity.status(201).body(goalService.createGoal(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable String id, @RequestBody GoalRequest request) {
        log.info("Cập nhật mục tiêu id: {}", id);
        return ResponseEntity.ok(goalService.updateGoal(id, request));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> depositToGoal(@PathVariable String id, @RequestBody DepositRequest request) {
        log.info("Nạp {} vào mục tiêu id {}", request.getAmount(), id);
        return ResponseEntity.ok(goalService.depositToGoal(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable String id) {
        log.info("Xóa mục tiêu id: {}", id);
        return ResponseEntity.ok(goalService.deleteGoal(id));
    }
}