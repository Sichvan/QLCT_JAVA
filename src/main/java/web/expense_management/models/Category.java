package web.expense_management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private String id;
    
    private String user; // Lưu ID của User (null nếu là danh mục mặc định)
    private String name;
    private String type; // 'expense', 'income', 'loan'
    private String icon;
    private boolean isDefault = false; // true: Hệ thống, false: User tự tạo
    
    private LocalDateTime createdAt = LocalDateTime.now();
}