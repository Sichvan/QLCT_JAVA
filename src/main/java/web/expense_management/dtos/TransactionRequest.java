package web.expense_management.dtos;


import java.util.Date;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class TransactionRequest {
    @NotBlank(message = "Loại giao dịch không được để trống")
    private String type; // 'expense', 'income', 'loan'

    @Min(value = 1, message = "Số tiền phải lớn hơn 0")
    private double amount;

    @NotBlank(message = "Danh mục không được để trống")
    private String category;

    private String categoryName;
    private String note;
    private String personName; // Tên người vay/cho vay (chỉ dùng cho loan)
    
    @NotNull(message = "Ngày giao dịch không được để trống")
    private Date date;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
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
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getPersonName() {
        return personName;
    }
    public void setPersonName(String personName) {
        this.personName = personName;
    }
}