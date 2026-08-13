package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.models.Expense;
import web.expense_management.models.Income;
import web.expense_management.models.Loan;
import web.expense_management.repositories.ExpenseRepository;
import web.expense_management.repositories.IncomeRepository;
import web.expense_management.repositories.LoanRepository;
import web.expense_management.utils.SecurityUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final LoanRepository loanRepository;

    public byte[] generateCsv(String filter, String dateStr) {
        String userId = SecurityUtils.getCurrentUserId();
        Date[] dateRange = getDateRange(filter, dateStr);

        List<Expense> expenses;
        List<Income> incomes;
        List<Loan> loans;

        if (dateRange != null) {
            expenses = expenseRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            incomes = incomeRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
            loans = loanRepository.findByUserAndDateBetween(userId, dateRange[0], dateRange[1]);
        } else {
            expenses = expenseRepository.findByUserOrderByDateDesc(userId);
            incomes = incomeRepository.findByUserOrderByDateDesc(userId);
            loans = loanRepository.findByUserOrderByDateDesc(userId);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF"); // BOM for UTF-8 encoding in Excel
        csv.append("Ngày,Danh mục,Loại,Số tiền,Ghi chú,Người vay/nợ\n");

        for (Expense e : expenses) {
            csv.append(String.format("\"%s\",\"%s\",\"Chi tiêu\",\"%,.0f\",\"%s\",\"\"\n",
                    sdf.format(e.getDate()), e.getCategoryName() != null ? e.getCategoryName() : e.getCategory(),
                    e.getAmount(), e.getNote() != null ? e.getNote() : ""));
        }
        for (Income i : incomes) {
            csv.append(String.format("\"%s\",\"%s\",\"Thu nhập\",\"%,.0f\",\"%s\",\"\"\n",
                    sdf.format(i.getDate()), i.getCategoryName() != null ? i.getCategoryName() : i.getCategory(),
                    i.getAmount(), i.getNote() != null ? i.getNote() : ""));
        }
        for (Loan l : loans) {
            String loanType = "lending".equals(l.getCategory()) || "repaying".equals(l.getCategory()) ? "Chi (Vay/Nợ)" : "Thu (Vay/Nợ)";
            csv.append(String.format("\"%s\",\"%s\",\"%s\",\"%,.0f\",\"%s\",\"%s\"\n",
                    sdf.format(l.getDate()), l.getCategoryName() != null ? l.getCategoryName() : l.getCategory(),
                    loanType, l.getAmount(), l.getNote() != null ? l.getNote() : "",
                    l.getPersonName() != null ? l.getPersonName() : ""));
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private Date[] getDateRange(String filter, String dateStr) {
        if (filter == null || "all".equals(filter)) return null;

        try {
            Calendar cal = Calendar.getInstance();

            if ("day".equals(filter) && dateStr != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date target = sdf.parse(dateStr);
                cal.setTime(target);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date start = cal.getTime();
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                Date end = cal.getTime();
                return new Date[]{start, end};
            } else if ("month".equals(filter) && dateStr != null) {
                String[] parts = dateStr.split("-");
                cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date start = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                Date end = cal.getTime();
                return new Date[]{start, end};
            } else if ("year".equals(filter) && dateStr != null) {
                int year = Integer.parseInt(dateStr);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date start = cal.getTime();
                cal.set(Calendar.MONTH, 11);
                cal.set(Calendar.DAY_OF_MONTH, 31);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                Date end = cal.getTime();
                return new Date[]{start, end};
            }
        } catch (Exception e) {
            log.error("Lỗi khi parse ngày: {}", e.getMessage());
        }
        return null;
    }
}
