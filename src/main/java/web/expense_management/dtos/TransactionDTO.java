package web.expense_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private String id;
    private String type; // 'expense', 'income', 'loan'
    private double amount;
    private String category;
    private String categoryName;
    private String note;
    private Date date;
}