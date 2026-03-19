package web.expense_management.services;

import org.springframework.stereotype.Service;
import web.expense_management.dtos.RegisterRequest;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    // Lưu mã OTP tạm thời (Key: Email, Value: Mã OTP)
    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    // Lưu thời hạn của OTP (Key: Email, Value: Thời gian hết hạn)
    private final ConcurrentHashMap<String, Long> otpExpiry = new ConcurrentHashMap<>();
    
    // BỘ NHỚ TẠM: Lưu thông tin người dùng đang chờ nhập OTP
    private final ConcurrentHashMap<String, RegisterRequest> pendingUsers = new ConcurrentHashMap<>();

    // Sinh mã OTP 6 số ngẫu nhiên
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + 5 * 60 * 1000); // Hết hạn sau 5 phút
        return otp;
    }

    // Kiểm tra OTP hợp lệ không
    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false; // Không có mã

        if (System.currentTimeMillis() > otpExpiry.get(email)) { // Quá hạn 5 phút
            clearOtp(email);
            return false;
        }

        boolean isValid = otpStorage.get(email).equals(otp);
        if (isValid) {
            clearOtp(email); // Đúng thì xóa mã luôn để bảo mật
        }
        return isValid;
    }

    // Xóa OTP
    private void clearOtp(String email) {
        otpStorage.remove(email);
        otpExpiry.remove(email);
    }

    // --- QUẢN LÝ NGƯỜI DÙNG CHỜ XÁC NHẬN ---
    public void savePendingUser(String email, RegisterRequest request) {
        pendingUsers.put(email, request);
    }

    public RegisterRequest getPendingUser(String email) {
        return pendingUsers.get(email);
    }

    public void removePendingUser(String email) {
        pendingUsers.remove(email);
    }
}