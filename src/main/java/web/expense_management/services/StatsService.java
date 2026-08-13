package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.ChartDataItem;
import web.expense_management.models.Expense;
import web.expense_management.models.Income;
import web.expense_management.models.Loan;
import web.expense_management.repositories.ExpenseRepository;
import web.expense_management.repositories.IncomeRepository;
import web.expense_management.repositories.LoanRepository;
import web.expense_management.utils.SecurityUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final LoanRepository loanRepository;

    private Date[] getDateRange(String startDateStr, String endDateStr) {
        try {
            if (startDateStr != null && endDateStr != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return new Date[]{sdf.parse(startDateStr), sdf.parse(endDateStr)};
            }
        } catch (Exception e) {
            log.error("Lỗi parse ngày trong StatsService: {}", e.getMessage());
        }
        
        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date start = cal.getTime();
        return new Date[]{start, end};
    }

    public Map<String, List<ChartDataItem>> getChartData(String startDate, String endDate) {
        String userId = SecurityUtils.getCurrentUserId();
        Date[] dateRange = getDateRange(startDate, endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
        List<Income> incomes = incomeRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
        List<Loan> loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);

        Map<String, Double> incomeMap = new HashMap<>();
        incomes.forEach(i -> incomeMap.merge(sdf.format(i.getDate()), i.getAmount(), Double::sum));
        loans.stream().filter(l -> Arrays.asList("borrowing", "collecting").contains(l.getCategory()))
             .forEach(l -> incomeMap.merge(sdf.format(l.getDate()), l.getAmount(), Double::sum));

        Map<String, Double> expenseMap = new HashMap<>();
        expenses.forEach(e -> expenseMap.merge(sdf.format(e.getDate()), e.getAmount(), Double::sum));
        loans.stream().filter(l -> Arrays.asList("lending", "repaying").contains(l.getCategory()))
             .forEach(l -> expenseMap.merge(sdf.format(l.getDate()), l.getAmount(), Double::sum));

        List<ChartDataItem> finalIncome = incomeMap.entrySet().stream()
                .map(e -> new ChartDataItem(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ChartDataItem::getId)).collect(Collectors.toList());
                
        List<ChartDataItem> finalExpense = expenseMap.entrySet().stream()
                .map(e -> new ChartDataItem(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ChartDataItem::getId)).collect(Collectors.toList());

        return Map.of("income", finalIncome, "expense", finalExpense);
    }

    public List<ChartDataItem> getPieChart(String type, String startDate, String endDate) {
        String userId = SecurityUtils.getCurrentUserId();
        Date[] dateRange = getDateRange(startDate, endDate);
        Map<String, Double> categoryMap = new HashMap<>();

        if ("expense".equals(type)) {
            List<Expense> expenses = expenseRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            expenses.forEach(e -> categoryMap.merge(e.getCategory(), e.getAmount(), Double::sum));
            
            List<Loan> loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            loans.stream().filter(l -> Arrays.asList("lending", "repaying").contains(l.getCategory()))
                 .forEach(l -> categoryMap.merge(l.getCategory(), l.getAmount(), Double::sum));
        } else if ("income".equals(type)) {
            List<Income> incomes = incomeRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            incomes.forEach(i -> categoryMap.merge(i.getCategory(), i.getAmount(), Double::sum));
            
            List<Loan> loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            loans.stream().filter(l -> Arrays.asList("borrowing", "collecting").contains(l.getCategory()))
                 .forEach(l -> categoryMap.merge(l.getCategory(), l.getAmount(), Double::sum));
        } else {
            throw new RuntimeException("Loại không hợp lệ");
        }

        return categoryMap.entrySet().stream()
                .map(e -> new ChartDataItem(e.getKey(), e.getValue()))
                .sorted((a, b) -> Double.compare(b.getTotal(), a.getTotal()))
                .collect(Collectors.toList());
    }
}
