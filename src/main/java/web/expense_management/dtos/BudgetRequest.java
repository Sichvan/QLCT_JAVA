package web.expense_management.dtos;

import lombok.Data;

@Data
public class BudgetRequest {
    private String category;
    private String categoryName;
    private double limitAmount;
}