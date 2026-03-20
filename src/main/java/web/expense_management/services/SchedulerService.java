package web.expense_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import web.expense_management.models.*;
import web.expense_management.repositories.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired private RecurringRepository recurringRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private IncomeRepository incomeRepository;

    // Đã thêm zone = "Asia/Ho_Chi_Minh" để chạy chuẩn 00:01 đêm giờ Việt Nam
    @Scheduled(cron = "0 1 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void runDailyRecurringTasks() {
        System.out.println("--- BẮT ĐẦU QUÉT GIAO DỊCH ĐỊNH KỲ ---");
        
        int currentDay = LocalDate.now().getDayOfMonth();
        List<Recurring> tasks = recurringRepository.findByDayOfMonthAndIsActiveTrue(currentDay);

        if (tasks.isEmpty()) {
            System.out.println("Không có giao dịch nào hôm nay.");
            return;
        }

        for (Recurring task : tasks) {
            User user = userRepository.findById(task.getUser()).orElse(null);
            if (user == null) continue;

            if ("expense".equals(task.getType())) {
                Expense expense = new Expense();
                expense.setUser(user.getId());
                expense.setAmount(task.getAmount());
                expense.setCategory(task.getCategory());
                expense.setCategoryName(task.getCategoryName() != null ? task.getCategoryName() : "");
                expense.setNote("[Định kỳ] " + (task.getNote() != null ? task.getNote() : ""));
                expense.setDate(new Date());
                expenseRepository.save(expense);
                user.setBalance(user.getBalance() - task.getAmount());
            } else {
                Income income = new Income();
                income.setUser(user.getId());
                income.setAmount(task.getAmount());
                income.setCategory(task.getCategory());
                income.setCategoryName(task.getCategoryName() != null ? task.getCategoryName() : "");
                income.setNote("[Định kỳ] " + (task.getNote() != null ? task.getNote() : ""));
                income.setDate(new Date());
                incomeRepository.save(income);
                user.setBalance(user.getBalance() + task.getAmount());
            }
            userRepository.save(user);
            System.out.println("Đã tạo giao dịch định kỳ cho user: " + user.getFullName());
        }
    }
}