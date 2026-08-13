package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.BudgetRequest;
import web.expense_management.dtos.BudgetResponse;
import web.expense_management.models.Budget;
import web.expense_management.models.Expense;
import web.expense_management.repositories.BudgetRepository;
import web.expense_management.repositories.ExpenseRepository;
import web.expense_management.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public List<BudgetResponse> getBudgets(Integer month, Integer year) {
        String userId = SecurityUtils.getCurrentUserId();
        List<Budget> budgets = budgetRepository.findByUser(userId);
        List<BudgetResponse> responseList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        if (month != null && year != null) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
        }
        
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfMonth = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfMonth = cal.getTime();

        for (Budget b : budgets) {
            List<Expense> expenses = expenseRepository.findByUserAndCategoryAndDateBetween(
                    userId, b.getCategory(), startOfMonth, endOfMonth);
            
            double spent = expenses.stream().mapToDouble(Expense::getAmount).sum();

            BudgetResponse dto = new BudgetResponse();
            dto.setId(b.getId());
            dto.setCategory(b.getCategory());
            dto.setCategoryName(b.getCategoryName());
            dto.setLimitAmount(b.getLimitAmount());
            dto.setSpentAmount(spent);
            dto.setExceeded(spent > b.getLimitAmount());

            responseList.add(dto);
        }

        return responseList;
    }

    public Budget addOrUpdateBudget(BudgetRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        
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

        return budgetRepository.save(budget);
    }

    public Map<String, String> deleteBudget(String id) {
        Optional<Budget> budget = budgetRepository.findById(id);
        if (budget.isPresent() && budget.get().getUser().equals(SecurityUtils.getCurrentUserId())) {
            budgetRepository.delete(budget.get());
            return Map.of("message", "Đã xóa");
        }
        throw new RuntimeException("Không tìm thấy hoặc không có quyền xóa");
    }
}
