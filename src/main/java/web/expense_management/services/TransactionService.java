package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.TransactionDTO;
import web.expense_management.dtos.TransactionRequest;
import web.expense_management.dtos.WalletResponse;
import web.expense_management.models.Budget;
import web.expense_management.models.Expense;
import web.expense_management.models.Income;
import web.expense_management.models.Loan;
import web.expense_management.models.User;
import web.expense_management.repositories.BudgetRepository;
import web.expense_management.repositories.ExpenseRepository;
import web.expense_management.repositories.IncomeRepository;
import web.expense_management.repositories.LoanRepository;
import web.expense_management.repositories.UserRepository;
import web.expense_management.utils.SecurityUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final EmailService emailService;

    public Map<String, Object> createTransaction(TransactionRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User"));

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

                checkBudgetAndAlert(userId, request.getCategory(), amount, user);
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
                loan.setPersonName(request.getPersonName());
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
                throw new RuntimeException("Loại giao dịch không hợp lệ");
        }

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Thêm giao dịch thành công");
        response.put("data", newTransaction);
        response.put("updatedBalance", user.getBalance());
        return response;
    }

    private void checkBudgetAndAlert(String userId, String category, double latestAmount, User user) {
        Optional<Budget> budgetOpt = budgetRepository.findByUserAndCategory(userId, category);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date startOfMonth = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endOfMonth = cal.getTime();

            List<Expense> monthlyExpenses = expenseRepository.findByUserAndCategoryAndDateBetween(userId, category, startOfMonth, endOfMonth);
            double totalSpent = monthlyExpenses.stream().mapToDouble(Expense::getAmount).sum();

            if (totalSpent > budget.getLimitAmount()) {
                String subject = "⚠️ CẢNH BÁO QUÁ NGÂN SÁCH - " + budget.getCategoryName();
                String body = "Chào " + user.getFullName() + ",\n\n"
                            + "Bạn vừa chi tiêu thêm " + String.format("%,.0f", latestAmount) + " đ vào danh mục " + budget.getCategoryName() + ".\n"
                            + "Hiện tại bạn đã chi tổng cộng: " + String.format("%,.0f", totalSpent) + " đ, vượt quá ngân sách cho phép (" + String.format("%,.0f", budget.getLimitAmount()) + " đ).\n\n"
                            + "Hãy chú ý cân nhắc và điều chỉnh việc chi tiêu nhé!\n\nTrân trọng,\nĐội ngũ ExpensePro.";
                emailService.sendEmail(user.getUsername(), subject, body);
            }
        }
    }

    public WalletResponse getWalletTransactions() {
        String userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow();

        List<Expense> expenses = expenseRepository.findByUserOrderByDateDesc(userId);
        List<Income> incomes = incomeRepository.findByUserOrderByDateDesc(userId);
        List<Loan> loans = loanRepository.findByUserOrderByDateDesc(userId);

        List<TransactionDTO> allTransactions = new ArrayList<>();
        
        for (Expense e : expenses) {
            allTransactions.add(new TransactionDTO(e.getId(), "expense", e.getAmount(), e.getCategory(), e.getCategoryName(), e.getNote(), null, e.getDate()));
        }
        for (Income i : incomes) {
            allTransactions.add(new TransactionDTO(i.getId(), "income", i.getAmount(), i.getCategory(), i.getCategoryName(), i.getNote(), null, i.getDate()));
        }
        for (Loan l : loans) {
            allTransactions.add(new TransactionDTO(l.getId(), "loan", l.getAmount(), l.getCategory(), l.getCategoryName(), l.getNote(), l.getPersonName(), l.getDate()));
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
        response.setTotalLending(Math.max(totalLending, 0));
        response.setTotalBorrowing(Math.max(totalBorrowing, 0));
        response.setMonthIncome(monthIncome);
        response.setMonthExpense(monthExpense);
        response.setTransactions(allTransactions);

        return response;
    }

    public Map<String, Object> updateTransaction(String id, TransactionRequest request) {
        String userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow();

        Expense oldExpense = expenseRepository.findById(id).orElse(null);
        Income oldIncome = incomeRepository.findById(id).orElse(null);
        Loan oldLoan = loanRepository.findById(id).orElse(null);

        if (oldExpense == null && oldIncome == null && oldLoan == null) {
            throw new RuntimeException("Không tìm thấy giao dịch");
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
                loan.setPersonName(request.getPersonName());
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
                throw new RuntimeException("Loại không hợp lệ");
        }

        userRepository.save(user);
        return Map.of("message", "Cập nhật thành công", "updatedBalance", user.getBalance());
    }

    public Map<String, Object> deleteTransaction(String id) {
        String userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow();

        Expense oldExpense = expenseRepository.findById(id).orElse(null);
        Income oldIncome = incomeRepository.findById(id).orElse(null);
        Loan oldLoan = loanRepository.findById(id).orElse(null);

        if (oldExpense == null && oldIncome == null && oldLoan == null) {
            throw new RuntimeException("Không tìm thấy giao dịch");
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
        return Map.of("message", "Đã xóa giao dịch", "updatedBalance", user.getBalance());
    }
}
