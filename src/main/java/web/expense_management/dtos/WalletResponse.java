package web.expense_management.dtos;

import lombok.Data;
import java.util.List;

@Data
public class WalletResponse {
    private double balance;
    private double totalLending;
    private double totalBorrowing;
    private double monthIncome;
    private double monthExpense;
    private List<TransactionDTO> transactions;
}