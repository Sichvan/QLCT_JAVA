package web.expense_management.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendEmail(String to, String subject, String body) {
        String url = "https://api.resend.com/emails";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        
        // Bắt buộc dùng email này nếu chưa có tên miền riêng
        requestBody.put("from", "ExpensePro <onboarding@resend.dev>");
        
        // Theo luật của Resend Free, biến 'to' này phải là email bạn đã đăng ký Resend
        requestBody.put("to", to); 
        requestBody.put("subject", subject);
        
        // Chuyển ký tự xuống dòng (\n) thành thẻ <br> để email hiển thị đẹp hơn
        requestBody.put("html", body.replace("\n", "<br>")); 

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            System.out.println("🚀 Đã gửi email OTP qua Resend API thành công tới: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi gửi email qua Resend: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email OTP qua API: " + e.getMessage());
        }
    }
}