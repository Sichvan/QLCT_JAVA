package web.expense_management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "budgets")
public class Budget {
    @Id
    private String id;
    
    private String user;
    private String category; // Lưu key icon định danh (ví dụ: eating)
    private String categoryName; // Tên hiển thị (ví dụ: Ăn uống)
    private double limitAmount; // Hạn mức
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}