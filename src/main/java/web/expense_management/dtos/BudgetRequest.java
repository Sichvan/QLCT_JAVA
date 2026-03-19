package web.expense_management.dtos;

public class BudgetRequest {
    private String category;
    private String categoryName;
    private double limitAmount;
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public double getLimitAmount() {
        return limitAmount;
    }
    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }
}