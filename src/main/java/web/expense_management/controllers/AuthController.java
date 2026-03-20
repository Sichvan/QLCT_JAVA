package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.AuthResponse;
import web.expense_management.dtos.LoginRequest;
import web.expense_management.dtos.RegisterRequest;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;
import web.expense_management.security.JwtUtils;
import web.expense_management.services.EmailService;
import web.expense_management.services.OtpService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils; 
    @Autowired private EmailService emailService;
    @Autowired private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Vui lòng nhập đầy đủ thông tin"));
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email này đã được sử dụng"));
        }

        String email = request.getUsername().toLowerCase().trim();
        otpService.savePendingUser(email, request);

        String otp = otpService.generateOtp(email);
        String subject = "Mã xác nhận đăng ký tài khoản QLCT_HK";
        String body = "Xin chào " + request.getFullName() + ",\n\n"
                    + "Mã OTP xác nhận đăng ký tài khoản của bạn là: " + otp + "\n"
                    + "Mã này sẽ hết hạn sau 5 phút.\n\n"
                    + "Trân trọng,\nĐội ngũ QLCT_HK";
        
        emailService.sendEmail(email, subject, body);

        return ResponseEntity.ok(Map.of("message", "Mã OTP đã được gửi. Vui lòng kiểm tra email của bạn."));
    }

    @PostMapping("/verify-register")
    public ResponseEntity<?> verifyRegister(@RequestBody Map<String, String> request) {
        String email = request.get("email").toLowerCase().trim();
        String otp = request.get("otp");

        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP không hợp lệ hoặc đã hết hạn"));
        }

        RegisterRequest pendingUser = otpService.getPendingUser(email);
        if (pendingUser == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Không tìm thấy phiên đăng ký, vui lòng đăng ký lại"));
        }

        User newUser = new User();
        newUser.setUsername(pendingUser.getUsername().toLowerCase().trim());
        newUser.setPassword(passwordEncoder.encode(pendingUser.getPassword()));
        newUser.setFullName(pendingUser.getFullName().trim());
        newUser.setPhone(pendingUser.getPhone());
        newUser.setRole("user");
        newUser.setCreatedAt(LocalDateTime.now());

        userRepository.save(newUser);
        otpService.removePendingUser(email); 

        String token = jwtUtils.generateToken(newUser);
        return ResponseEntity.status(201).body(new AuthResponse(newUser, token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername().toLowerCase().trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtils.generateToken(user);
                return ResponseEntity.ok(new AuthResponse(user, token));
            }
        }
        return ResponseEntity.status(401).body(Map.of("message", "Email hoặc mật khẩu không chính xác"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || !userRepository.existsByUsername(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email không tồn tại trong hệ thống"));
        }

        String otp = otpService.generateOtp(email);
        String body = "Bạn đã yêu cầu khôi phục mật khẩu.\n\n"
                    + "Mã OTP của bạn là: " + otp + "\n"
                    + "Vui lòng nhập mã này vào ứng dụng để đặt lại mật khẩu mới (Mã có hiệu lực trong 5 phút).\n\n"
                    + "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.";
        
        emailService.sendEmail(email, "Khôi phục mật khẩu QLCT_HK", body);
        return ResponseEntity.ok(Map.of("message", "Đã gửi mã OTP khôi phục về email của bạn."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP không hợp lệ hoặc đã hết hạn"));
        }

        if (newPassword == null || !newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu mới phải có ít nhất 6 ký tự, bao gồm chữ và số"));
        }

        User user = userRepository.findByUsername(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công. Vui lòng đăng nhập lại."));
    }
}