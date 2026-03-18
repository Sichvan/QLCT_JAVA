package web.expense_management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "goals")
public class Goal {
    @Id
    private String id;
    
    private String user; // Lưu ID của User
    private String name;
    private double targetAmount;
    private double currentAmount = 0.0;
    private String status = "ongoing"; // 'ongoing' hoặc 'completed'
    private LocalDateTime deadline;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}