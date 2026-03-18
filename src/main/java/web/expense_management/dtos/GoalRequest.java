package web.expense_management.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GoalRequest {
    private String name;
    private double targetAmount;
    private LocalDateTime deadline;
}