package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.BudgetRequest;
import web.expense_management.dtos.BudgetResponse;
import web.expense_management.models.Budget;
import web.expense_management.models.Expense;
import web.expense_management.models.User;
import web.expense_management.repositories.BudgetRepository;
import web.expense_management.repositories.ExpenseRepository;
import java.time.LocalDateTime;

import java.util.*;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired private BudgetRepository budgetRepository;
    @Autowired private ExpenseRepository expenseRepository;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    // 1. LẤY DANH SÁCH NGÂN SÁCH (Có tính toán số tiền đã chi)
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        
        String userId = getCurrentUserId();
        List<Budget> budgets = budgetRepository.findByUser(userId);
        List<BudgetResponse> responseList = new ArrayList<>();

        // Xác định khoảng thời gian cần tính toán
        Calendar cal = Calendar.getInstance();
        if (month != null && year != null) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1); // Calendar month chạy từ 0-11
        }
        
        // Ngày đầu tháng
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfMonth = cal.getTime();

        // Ngày cuối tháng
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfMonth = cal.getTime();

        // Duyệt qua từng ngân sách và tính tổng tiền đã chi
        for (Budget b : budgets) {
            List<Expense> expenses = expenseRepository.findByUserAndCategoryAndDateBetween(
                    userId, b.getCategory(), startOfMonth, endOfMonth);
            
            double spent = 0;
            for (Expense e : expenses) {
                spent += e.getAmount();
            }

            BudgetResponse dto = new BudgetResponse();
            dto.setId(b.getId());
            dto.setCategory(b.getCategory());
            dto.setCategoryName(b.getCategoryName());
            dto.setLimitAmount(b.getLimitAmount());
            dto.setSpentAmount(spent);
            dto.setExceeded(spent > b.getLimitAmount());

            responseList.add(dto);
        }

        return ResponseEntity.ok(responseList);
    }

    // 2. THÊM HOẶC CẬP NHẬT NGÂN SÁCH
    @PostMapping
    public ResponseEntity<?> addOrUpdateBudget(@RequestBody BudgetRequest request) {
        String userId = getCurrentUserId();
        
        // Kiểm tra xem user đã có ngân sách cho category này chưa
        Optional<Budget> existingBudget = budgetRepository.findByUserAndCategory(userId, request.getCategory());
        Budget budget;

        if (existingBudget.isPresent()) {
            budget = existingBudget.get();
            budget.setLimitAmount(request.getLimitAmount());
            budget.setCategoryName(request.getCategoryName());
            budget.setUpdatedAt(LocalDateTime.now());
        } else {
            budget = new Budget();
            budget.setUser(userId);
            budget.setCategory(request.getCategory());
            budget.setCategoryName(request.getCategoryName());
            budget.setLimitAmount(request.getLimitAmount());
        }

        budgetRepository.save(budget);
        return ResponseEntity.status(201).body(budget);
    }

    // 3. XÓA NGÂN SÁCH
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable String id) {
        Optional<Budget> budget = budgetRepository.findById(id);
        if (budget.isPresent() && budget.get().getUser().equals(getCurrentUserId())) {
            budgetRepository.delete(budget.get());
            return ResponseEntity.ok(Map.of("message", "Đã xóa"));
        }
        return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy hoặc không có quyền xóa"));
    }
}