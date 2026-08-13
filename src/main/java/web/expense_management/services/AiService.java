package web.expense_management.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String getFinancialAdvice(String userMessage, String financialContext, String lang) {
        // Chỉ dùng duy nhất mô hình Nhanh & Miễn phí (gemini-2.5-flash)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String systemPrompt = "Bạn là chuyên gia tài chính thân thiện của ứng dụng QLCT_HK. " +
                "Tình hình tài chính của người dùng hiện tại: " + financialContext + ". " +
                "Câu hỏi của người dùng: '" + userMessage + "'. " +
                "Hãy trả lời xưng 'mình' và gọi 'bạn', đưa ra lời khuyên thực tế, format câu trả lời rõ ràng (dùng Markdown như in đậm, gạch đầu dòng).";

        if ("en".equals(lang)) {
            systemPrompt += " IMPORTANT: You MUST reply entirely in English. Translate your financial advice into English.";
        }

        Map<String, Object> part = new HashMap<>();
        part.put("text", systemPrompt);
        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(part));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(content));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            Map<String, Object> contentRes = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentRes.get("parts");
            
            return (String) parts.get(0).get("text");
        } catch (HttpClientErrorException e) {
            // Nếu có lỗi, in ra Terminal để dev dễ kiểm tra, còn User sẽ thấy thông báo lịch sự
            System.err.println("LỖI GOOGLE: " + e.getResponseBodyAsString());
            return "en".equals(lang) ? "Sorry, the AI server is busy or overloaded. Please try again later!" : "Xin lỗi, hiện tại máy chủ AI đang bận hoặc quá tải. Bạn vui lòng thử lại sau nhé!";
        } catch (Exception e) {
            System.err.println("LỖI HỆ THỐNG: " + e.getMessage());
            return "en".equals(lang) ? "Sorry, the AI server is busy. Please try again later!" : "Xin lỗi, hiện tại máy chủ AI đang bận. Bạn vui lòng thử lại sau nhé!";
        }
    }
}