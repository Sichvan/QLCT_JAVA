package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.ChartDataItem;
import web.expense_management.models.Expense;
import web.expense_management.models.Income;
import web.expense_management.models.Loan;
import web.expense_management.models.User;
import web.expense_management.repositories.ExpenseRepository;
import web.expense_management.repositories.IncomeRepository;
import web.expense_management.repositories.LoanRepository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private IncomeRepository incomeRepository;
    @Autowired private LoanRepository loanRepository;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    // --- HÀM HỖ TRỢ LẤY KHOẢNG THỜI GIAN ---
    private Date[] getDateRange(String startDateStr, String endDateStr) {
        try {
            if (startDateStr != null && endDateStr != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return new Date[]{sdf.parse(startDateStr), sdf.parse(endDateStr)};
            }
        } catch (Exception e) {}
        
        // Mặc định: 30 ngày qua
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date start = cal.getTime();
        return new Date[]{start, end};
    }

    // 1. BIỂU ĐỒ ĐƯỜNG (LINE/BAR CHART) - TỔNG HỢP THU CHI THEO NGÀY
    @GetMapping("/chart-data")
    public ResponseEntity<?> getChartData(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        String userId = getCurrentUserId();
        Date[] dateRange = getDateRange(startDate, endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
        List<Income> incomes = incomeRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
        List<Loan> loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);

        // Gom nhóm Thu nhập (Income + Borrowing + Collecting)
        Map<String, Double> incomeMap = new HashMap<>();
        incomes.forEach(i -> incomeMap.merge(sdf.format(i.getDate()), i.getAmount(), Double::sum));
        loans.stream().filter(l -> Arrays.asList("borrowing", "collecting").contains(l.getCategory()))
             .forEach(l -> incomeMap.merge(sdf.format(l.getDate()), l.getAmount(), Double::sum));

        // Gom nhóm Chi tiêu (Expense + Lending + Repaying)
        Map<String, Double> expenseMap = new HashMap<>();
        expenses.forEach(e -> expenseMap.merge(sdf.format(e.getDate()), e.getAmount(), Double::sum));
        loans.stream().filter(l -> Arrays.asList("lending", "repaying").contains(l.getCategory()))
             .forEach(l -> expenseMap.merge(sdf.format(l.getDate()), l.getAmount(), Double::sum));

        // Format lại dữ liệu trả về
        List<ChartDataItem> finalIncome = incomeMap.entrySet().stream()
                .map(e -> new ChartDataItem(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ChartDataItem::getId)).collect(Collectors.toList());
                
        List<ChartDataItem> finalExpense = expenseMap.entrySet().stream()
                .map(e -> new ChartDataItem(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ChartDataItem::getId)).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("income", finalIncome, "expense", finalExpense));
    }

    // 2. BIỂU ĐỒ TRÒN (PIE CHART) - TỔNG HỢP THEO DANH MỤC
    @GetMapping("/pie-chart")
    public ResponseEntity<?> getPieChart(
            @RequestParam String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        String userId = getCurrentUserId();
        Date[] dateRange = getDateRange(startDate, endDate);
        Map<String, Double> categoryMap = new HashMap<>();

        if ("expense".equals(type)) {
            List<Expense> expenses = expenseRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            expenses.forEach(e -> categoryMap.merge(e.getCategory(), e.getAmount(), Double::sum));
            
            List<Loan> loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            loans.stream().filter(l -> Arrays.asList("lending", "repaying").contains(l.getCategory()))
                 .forEach(l -> categoryMap.merge(l.getCategory(), l.getAmount(), Double::sum));
        } 
        else if ("income".equals(type)) {
            List<Income> incomes = incomeRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            incomes.forEach(i -> categoryMap.merge(i.getCategory(), i.getAmount(), Double::sum));
            
            List<Loan> loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            loans.stream().filter(l -> Arrays.asList("borrowing", "collecting").contains(l.getCategory()))
                 .forEach(l -> categoryMap.merge(l.getCategory(), l.getAmount(), Double::sum));
        } 
        else {
            return ResponseEntity.badRequest().body(Map.of("message", "Loại không hợp lệ"));
        }

        List<ChartDataItem> result = categoryMap.entrySet().stream()
                .map(e -> new ChartDataItem(e.getKey(), e.getValue()))
                .sorted((a, b) -> Double.compare(b.getTotal(), a.getTotal())) // Sắp xếp giảm dần
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}