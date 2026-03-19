package web.expense_management.dtos;

public class ChartDataItem {
    private String id; // Chứa Ngày (YYYY-MM-DD) hoặc Tên danh mục
    private double total;

    // Hàm khởi tạo rỗng
    public ChartDataItem() {
    }

    // Hàm khởi tạo có tham số (Để sửa lỗi ở StatsController)
    public ChartDataItem(String id, double total) {
        this.id = id;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}