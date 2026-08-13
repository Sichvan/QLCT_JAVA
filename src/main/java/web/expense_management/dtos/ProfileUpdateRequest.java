package web.expense_management.dtos;

import jakarta.validation.constraints.Pattern;

public class ProfileUpdateRequest {
    private String fullName;

    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "Mật khẩu mới phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số")
    private String newPassword;
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}