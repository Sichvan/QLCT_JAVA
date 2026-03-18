package web.expense_management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data // Annotation của Lombok tự động tạo Getter/Setter
@Document(collection = "users") // Tương đương mongoose.model('User', userSchema)
public class User {
    
    @Id
    private String id; // Tương đương _id trong MongoDB
    
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private double balance = 0.0;
    private String role = "user"; // 'user' hoặc 'admin'
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}