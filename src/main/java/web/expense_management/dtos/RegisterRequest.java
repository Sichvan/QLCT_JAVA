package web.expense_management.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
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
}