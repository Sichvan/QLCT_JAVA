package web.expense_management.dtos;

public class BudgetResponse {
    private String id;
    private String category;
    private String categoryName;
    private double limitAmount;
    private double spentAmount;
    private boolean isExceeded;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public double getSpentAmount() {
        return spentAmount;
    }
    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }
    public boolean isExceeded() {
        return isExceeded;
    }
    public void setExceeded(boolean isExceeded) {
        this.isExceeded = isExceeded;
    }
}