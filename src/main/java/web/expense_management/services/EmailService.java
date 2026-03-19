package web.expense_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // Bạn nhớ thay email của bạn vào dòng dưới này nhé
            message.setFrom("ExpensePro <email_cua_ban@gmail.com>"); 
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Đã gửi email thành công tới: " + to);
        } catch (Exception e) {
            System.out.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}