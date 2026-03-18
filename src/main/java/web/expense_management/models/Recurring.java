package web.expense_management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "recurrings")
public class Recurring {
    @Id private String id;
    private String user;
    private String type;
    private double amount;
    private String category;
    private String categoryName;
    private int dayOfMonth;
    private String note;
    private boolean isActive = true;
}