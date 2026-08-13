package web.expense_management.dtos;

import java.util.Date;

public class TransactionDTO {
    private String id;
    private String type; // 'expense', 'income', 'loan'
    private double amount;
    private String category;
    private String categoryName;
    private String note;
    private String personName;
    private Date date;

    // 1. HÀM KHỞI TẠO RỖNG (Bắt buộc cho Spring Boot)
    public TransactionDTO() {
    }

    // 2. HÀM KHỞI TẠO CÓ THAM SỐ (Để sửa lỗi ở TransactionController)
    public TransactionDTO(String id, String type, double amount, String category, String categoryName, String note, String personName, Date date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.categoryName = categoryName;
        this.note = note;
        this.personName = personName;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
}