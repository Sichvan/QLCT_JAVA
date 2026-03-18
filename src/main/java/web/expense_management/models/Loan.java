package web.expense_management.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.Date;

@Data
@Document(collection = "loans")
public class Loan {
    @Id
    private String id;
    private String user;
    private double amount;
    private String category;
    private String categoryName;
    private String note;
    private Date date;
    private boolean isCompleted = false;
}