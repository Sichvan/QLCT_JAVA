package web.expense_management.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.models.Recurring;
import web.expense_management.services.RecurringService;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
@Slf4j
public class RecurringController {

    private final RecurringService recurringService;

    @GetMapping
    public ResponseEntity<List<Recurring>> getRecurrings() {
        log.info("Lấy danh sách giao dịch định kỳ");
        return ResponseEntity.ok(recurringService.getRecurrings());
    }

    @PostMapping
    public ResponseEntity<?> addRecurring(@RequestBody Recurring request) {
        log.info("Thêm giao dịch định kỳ mới");
        return ResponseEntity.status(201).body(recurringService.addRecurring(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecurring(@PathVariable String id, @RequestBody Recurring request) {
        log.info("Cập nhật giao dịch định kỳ id: {}", id);
        return ResponseEntity.ok(recurringService.updateRecurring(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecurring(@PathVariable String id) {
        log.info("Xóa giao dịch định kỳ id: {}", id);
        return ResponseEntity.ok(recurringService.deleteRecurring(id));
    }

    @GetMapping("/test-run")
    public ResponseEntity<?> testRunScheduler() {
        log.info("Test chạy schedule giao dịch định kỳ");
        return ResponseEntity.ok(recurringService.testRunScheduler());
    }
}