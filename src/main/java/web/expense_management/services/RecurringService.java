package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.models.Recurring;
import web.expense_management.repositories.RecurringRepository;
import web.expense_management.utils.SecurityUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecurringService {

    private final RecurringRepository recurringRepository;
    private final SchedulerService schedulerService;

    public List<Recurring> getRecurrings() {
        return recurringRepository.findByUserOrderByDayOfMonthAsc(SecurityUtils.getCurrentUserId());
    }

    public Recurring addRecurring(Recurring request) {
        request.setUser(SecurityUtils.getCurrentUserId());
        request.setActive(true);
        return recurringRepository.save(request);
    }

    public Recurring updateRecurring(String id, Recurring request) {
        Recurring recurring = recurringRepository.findById(id).orElseThrow();
        if (!recurring.getUser().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Không có quyền");
        }

        recurring.setType(request.getType());
        recurring.setAmount(request.getAmount());
        recurring.setCategory(request.getCategory());
        recurring.setCategoryName(request.getCategoryName());
        recurring.setDayOfMonth(request.getDayOfMonth());
        recurring.setNote(request.getNote());
        
        return recurringRepository.save(recurring);
    }

    public Map<String, String> deleteRecurring(String id) {
        Recurring recurring = recurringRepository.findById(id).orElseThrow();
        if (recurring.getUser().equals(SecurityUtils.getCurrentUserId())) {
            recurringRepository.delete(recurring);
        }
        return Map.of("message", "Đã xóa");
    }

    public Map<String, String> testRunScheduler() {
        schedulerService.runDailyRecurringTasks();
        return Map.of("message", "Đã chạy thử quét giao dịch định kỳ thành công! Hãy check lại ví.");
    }
}
