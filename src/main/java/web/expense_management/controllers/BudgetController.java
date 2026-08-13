package web.expense_management.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.BudgetRequest;
import web.expense_management.dtos.BudgetResponse;
import web.expense_management.services.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Slf4j
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        log.info("Lấy danh sách ngân sách: month={}, year={}", month, year);
        return ResponseEntity.ok(budgetService.getBudgets(month, year));
    }

    @PostMapping
    public ResponseEntity<?> addOrUpdateBudget(@RequestBody BudgetRequest request) {
        log.info("Thêm hoặc cập nhật ngân sách cho danh mục: {}", request.getCategory());
        return ResponseEntity.status(201).body(budgetService.addOrUpdateBudget(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable String id) {
        log.info("Xóa ngân sách id: {}", id);
        return ResponseEntity.ok(budgetService.deleteBudget(id));
    }
}