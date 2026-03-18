package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.TransactionDTO;
import web.expense_management.dtos.TransactionRequest;
import web.expense_management.dtos.WalletResponse;
import web.expense_management.models.Expense;
import web.expense_management.models.Income;
import web.expense_management.models.Loan;
import web.expense_management.models.User;
import web.expense_management.repositories.ExpenseRepository;
import web.expense_management.repositories.IncomeRepository;
import web.expense_management.repositories.LoanRepository;
import web.expense_management.repositories.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private IncomeRepository incomeRepository;
    @Autowired private LoanRepository loanRepository;
    @Autowired private UserRepository userRepository;

    // --- HÀM HỖ TRỢ: LẤY ID USER ĐANG ĐĂNG NHẬP ---
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    // --- 1. TẠO GIAO DỊCH MỚI (POST) ---
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest request) {
        if (request.getAmount() <= 0 || request.getCategory() == null || request.getType() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thiếu thông tin bắt buộc"));
        }

        String userId = getCurrentUserId();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy User"));
        
        User user = userOpt.get();
        Object newTransaction = null;
        double amount = request.getAmount();
        Date transDate = request.getDate() != null ? request.getDate() : new Date();

        switch (request.getType()) {
            case "expense":
                Expense expense = new Expense();
                expense.setUser(userId);
                expense.setAmount(amount);
                expense.setCategory(request.getCategory());
                expense.setCategoryName(request.getCategoryName());
                expense.setNote(request.getNote());
                expense.setDate(transDate);
                newTransaction = expenseRepository.save(expense);
                user.setBalance(user.getBalance() - amount);
                break;

            case "income":
                Income income = new Income();
                income.setUser(userId);
                income.setAmount(amount);
                income.setCategory(request.getCategory());
                income.setCategoryName(request.getCategoryName());
                income.setNote(request.getNote());
                income.setDate(transDate);
                newTransaction = incomeRepository.save(income);
                user.setBalance(user.getBalance() + amount);
                break;

            case "loan":
                Loan loan = new Loan();
                loan.setUser(userId);
                loan.setAmount(amount);
                loan.setCategory(request.getCategory());
                loan.setCategoryName(request.getCategoryName());
                loan.setNote(request.getNote());
                loan.setDate(transDate);
                newTransaction = loanRepository.save(loan);
                
                String cat = request.getCategory();
                if ("lending".equals(cat) || "repaying".equals(cat)) {
                    user.setBalance(user.getBalance() - amount);
                } else if ("borrowing".equals(cat) || "collecting".equals(cat)) {
                    user.setBalance(user.getBalance() + amount);
                }
                break;

            default:
                return ResponseEntity.badRequest().body(Map.of("message", "Loại giao dịch không hợp lệ"));
        }

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Thêm giao dịch thành công");
        response.put("data", newTransaction);
        response.put("updatedBalance", user.getBalance());

        return ResponseEntity.status(201).body(response);
    }

    // --- 2. LẤY DANH SÁCH & THỐNG KÊ VÍ (GET) ---
    @GetMapping
    public ResponseEntity<WalletResponse> getWalletTransactions() {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUserOrderByDateDesc(userId);
        List<Income> incomes = incomeRepository.findByUserOrderByDateDesc(userId);
        List<Loan> loans = loanRepository.findByUserOrderByDateDesc(userId);

        List<TransactionDTO> allTransactions = new ArrayList<>();
        
        for (Expense e : expenses) {
            allTransactions.add(new TransactionDTO(e.getId(), "expense", e.getAmount(), e.getCategory(), e.getCategoryName(), e.getNote(), e.getDate()));
        }
        for (Income i : incomes) {
            allTransactions.add(new TransactionDTO(i.getId(), "income", i.getAmount(), i.getCategory(), i.getCategoryName(), i.getNote(), i.getDate()));
        }
        for (Loan l : loans) {
            allTransactions.add(new TransactionDTO(l.getId(), "loan", l.getAmount(), l.getCategory(), l.getCategoryName(), l.getNote(), l.getDate()));
        }

        allTransactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        Calendar now = Calendar.getInstance();
        int currentMonth = now.get(Calendar.MONTH);
        int currentYear = now.get(Calendar.YEAR);

        double monthIncome = 0;
        double monthExpense = 0;
        double totalLending = 0;
        double totalBorrowing = 0;

        for (Loan loan : loans) {
            if ("lending".equals(loan.getCategory())) totalLending += loan.getAmount();
            else if ("collecting".equals(loan.getCategory())) totalLending -= loan.getAmount();
            else if ("borrowing".equals(loan.getCategory())) totalBorrowing += loan.getAmount();
            else if ("repaying".equals(loan.getCategory())) totalBorrowing -= loan.getAmount();
        }

        for (TransactionDTO t : allTransactions) {
            Calendar tDate = Calendar.getInstance();
            tDate.setTime(t.getDate());
            
            if (tDate.get(Calendar.MONTH) == currentMonth && tDate.get(Calendar.YEAR) == currentYear) {
                if ("income".equals(t.getType())) monthIncome += t.getAmount();
                if ("expense".equals(t.getType())) monthExpense += t.getAmount();
            }
        }

        WalletResponse response = new WalletResponse();
        response.setBalance(user.getBalance());
        response.setTotalLending(totalLending < 0 ? 0 : totalLending);
        response.setTotalBorrowing(totalBorrowing < 0 ? 0 : totalBorrowing);
        response.setMonthIncome(monthIncome);
        response.setMonthExpense(monthExpense);
        response.setTransactions(allTransactions);

        return ResponseEntity.ok(response);
    }

    // --- 3. SỬA GIAO DỊCH (PUT) ---
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable String id, @RequestBody TransactionRequest request) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow();

        Expense oldExpense = expenseRepository.findById(id).orElse(null);
        Income oldIncome = incomeRepository.findById(id).orElse(null);
        Loan oldLoan = loanRepository.findById(id).orElse(null);

        if (oldExpense == null && oldIncome == null && oldLoan == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy giao dịch"));
        }

        // Hoàn tác số dư cũ & Xóa
        if (oldExpense != null) {
            user.setBalance(user.getBalance() + oldExpense.getAmount());
            expenseRepository.delete(oldExpense);
        } else if (oldIncome != null) {
            user.setBalance(user.getBalance() - oldIncome.getAmount());
            incomeRepository.delete(oldIncome);
        } else if (oldLoan != null) {
            String cat = oldLoan.getCategory();
            if ("lending".equals(cat) || "repaying".equals(cat)) {
                user.setBalance(user.getBalance() + oldLoan.getAmount());
            } else {
                user.setBalance(user.getBalance() - oldLoan.getAmount());
            }
            loanRepository.delete(oldLoan);
        }

        // Tạo bản ghi mới
        double newAmount = request.getAmount();
        Date transDate = request.getDate() != null ? request.getDate() : new Date();

        switch (request.getType()) {
            case "expense":
                Expense expense = new Expense();
                expense.setId(id);
                expense.setUser(userId);
                expense.setAmount(newAmount);
                expense.setCategory(request.getCategory());
                expense.setCategoryName(request.getCategoryName());
                expense.setNote(request.getNote());
                expense.setDate(transDate);
                expenseRepository.save(expense);
                user.setBalance(user.getBalance() - newAmount);
                break;
            case "income":
                Income income = new Income();
                income.setId(id);
                income.setUser(userId);
                income.setAmount(newAmount);
                income.setCategory(request.getCategory());
                income.setCategoryName(request.getCategoryName());
                income.setNote(request.getNote());
                income.setDate(transDate);
                incomeRepository.save(income);
                user.setBalance(user.getBalance() + newAmount);
                break;
            case "loan":
                Loan loan = new Loan();
                loan.setId(id);
                loan.setUser(userId);
                loan.setAmount(newAmount);
                loan.setCategory(request.getCategory());
                loan.setCategoryName(request.getCategoryName());
                loan.setNote(request.getNote());
                loan.setDate(transDate);
                loanRepository.save(loan);
                String cat = request.getCategory();
                if ("lending".equals(cat) || "repaying".equals(cat)) {
                    user.setBalance(user.getBalance() - newAmount);
                } else {
                    user.setBalance(user.getBalance() + newAmount);
                }
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("message", "Loại không hợp lệ"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of(
            "message", "Cập nhật thành công", 
            "updatedBalance", user.getBalance()
        ));
    }

    // --- 4. XÓA GIAO DỊCH (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable String id) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow();

        Expense oldExpense = expenseRepository.findById(id).orElse(null);
        Income oldIncome = incomeRepository.findById(id).orElse(null);
        Loan oldLoan = loanRepository.findById(id).orElse(null);

        if (oldExpense == null && oldIncome == null && oldLoan == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy giao dịch"));
        }

        if (oldExpense != null) {
            user.setBalance(user.getBalance() + oldExpense.getAmount());
            expenseRepository.delete(oldExpense);
        } else if (oldIncome != null) {
            user.setBalance(user.getBalance() - oldIncome.getAmount());
            incomeRepository.delete(oldIncome);
        } else if (oldLoan != null) {
            String cat = oldLoan.getCategory();
            if ("lending".equals(cat) || "repaying".equals(cat)) {
                user.setBalance(user.getBalance() + oldLoan.getAmount());
            } else {
                user.setBalance(user.getBalance() - oldLoan.getAmount());
            }
            loanRepository.delete(oldLoan);
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of(
            "message", "Đã xóa giao dịch", 
            "updatedBalance", user.getBalance()
        ));
    }
}