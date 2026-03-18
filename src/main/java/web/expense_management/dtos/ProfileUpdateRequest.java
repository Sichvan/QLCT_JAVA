package web.expense_management.dtos;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String fullName;
    private String phone;
    private String currentPassword;
    private String newPassword;
}