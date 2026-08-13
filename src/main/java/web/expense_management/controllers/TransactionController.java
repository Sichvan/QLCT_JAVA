package web.expense_management.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.TransactionRequest;
import web.expense_management.dtos.WalletResponse;
import web.expense_management.services.TransactionService;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest request) {
        log.info("Tạo giao dịch mới: loại {}, số tiền {}", request.getType(), request.getAmount());
        Map<String, Object> response = transactionService.createTransaction(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWalletTransactions() {
        log.info("Lấy danh sách giao dịch cho ví");
        WalletResponse response = transactionService.getWalletTransactions();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable String id, @Valid @RequestBody TransactionRequest request) {
        log.info("Cập nhật giao dịch id {}", id);
        Map<String, Object> response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable String id) {
        log.info("Xóa giao dịch id {}", id);
        Map<String, Object> response = transactionService.deleteTransaction(id);
        return ResponseEntity.ok(response);
    }
}