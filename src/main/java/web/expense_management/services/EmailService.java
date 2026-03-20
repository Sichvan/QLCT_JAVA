package web.expense_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Tự động lấy email mà bạn đã cài trên Render (biến EMAIL_USER)
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // Cấu hình người gửi tự động
            message.setFrom("ExpensePro <" + senderEmail + ">"); 
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Đã gửi email thành công tới: " + to);
        } catch (Exception e) {
            // In thẳng lỗi ra Logs của Render để dễ bắt bệnh nếu gửi hỏng
            e.printStackTrace();
            System.out.println("Lỗi khi gửi email: " + e.getMessage());
            
            // Ném lỗi ra ngoài để AuthController biết mà báo về Frontend
            throw new RuntimeException("Không thể gửi email OTP: " + e.getMessage());
        }
    }
}