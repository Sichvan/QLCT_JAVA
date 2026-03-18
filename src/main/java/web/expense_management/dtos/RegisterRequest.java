package web.expense_management.dtos;

import lombok.Data;

@Data // Tự động tạo getter/setter
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String phone;
}