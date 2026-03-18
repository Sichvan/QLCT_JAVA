package web.expense_management.dtos;

import lombok.Data;
import web.expense_management.models.User;

@Data
public class AuthResponse {
    private User user;
    private String token;

    public AuthResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
}