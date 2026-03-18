package web.expense_management.dtos;

import lombok.Data;

@Data
public class BudgetResponse {
    private String id;
    private String category;
    private String categoryName;
    private double limitAmount;
    private double spentAmount;
    private boolean isExceeded;
}