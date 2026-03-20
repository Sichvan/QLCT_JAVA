package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.models.Recurring;
import web.expense_management.models.User;
import web.expense_management.repositories.RecurringRepository;
import web.expense_management.services.SchedulerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recurring")
public class RecurringController {

    @Autowired private RecurringRepository recurringRepository;
    
    // Tiêm SchedulerService vào để gọi hàm test
    @Autowired private SchedulerService schedulerService;

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((User) auth.getPrincipal()).getId();
    }

    @GetMapping
    public ResponseEntity<List<Recurring>> getRecurrings() {
        return ResponseEntity.ok(recurringRepository.findByUserOrderByDayOfMonthAsc(getCurrentUserId()));
    }

    @PostMapping
    public ResponseEntity<?> addRecurring(@RequestBody Recurring request) {
        request.setUser(getCurrentUserId());
        request.setActive(true);
        return ResponseEntity.status(201).body(recurringRepository.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecurring(@PathVariable String id, @RequestBody Recurring request) {
        Recurring recurring = recurringRepository.findById(id).orElseThrow();
        if (!recurring.getUser().equals(getCurrentUserId())) return ResponseEntity.status(403).build();

        recurring.setType(request.getType());
        recurring.setAmount(request.getAmount());
        recurring.setCategory(request.getCategory());
        recurring.setCategoryName(request.getCategoryName());
        recurring.setDayOfMonth(request.getDayOfMonth());
        recurring.setNote(request.getNote());
        
        return ResponseEntity.ok(recurringRepository.save(recurring));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecurring(@PathVariable String id) {
        Recurring recurring = recurringRepository.findById(id).orElseThrow();
        if (recurring.getUser().equals(getCurrentUserId())) {
            recurringRepository.delete(recurring);
        }
        return ResponseEntity.ok(Map.of("message", "Đã xóa"));
    }

    // NÚT BẤM BÍ MẬT DÀNH CHO DEV ĐỂ TEST CHỨC NĂNG ĐỊNH KỲ
    @GetMapping("/test-run")
    public ResponseEntity<?> testRunScheduler() {
        schedulerService.runDailyRecurringTasks();
        return ResponseEntity.ok(Map.of("message", "Đã chạy thử quét giao dịch định kỳ thành công! Hãy check lại ví."));
    }
}