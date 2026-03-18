package web.expense_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartDataItem {
    private String id; // Chứa Ngày (YYYY-MM-DD) hoặc Tên danh mục
    private double total;
}