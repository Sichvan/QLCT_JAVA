package web.expense_management.dtos;

import lombok.Data;
import java.util.Date;

@Data
public class TransactionRequest {
    private String type; // 'expense', 'income', 'loan'
    private double amount;
    private String category;
    private String categoryName;
    private String note;
    private Date date;
}