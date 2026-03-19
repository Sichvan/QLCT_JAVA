package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;
import web.expense_management.services.AiService;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private AiService aiService;

    @Autowired
    private UserRepository userRepository;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((User) authentication.getPrincipal()).getId();
    }

    @PostMapping
    public ResponseEntity<?> chatWithAi(@RequestBody Map<String, String> request) {
        String message = request.get("message");

        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("reply", "Bạn chưa nhập câu hỏi."));
        }

        User user = userRepository.findById(getCurrentUserId()).orElseThrow();
        String context = String.format("Tên: %s. Số dư ví hiện tại: %,.0f VNĐ.", user.getFullName(), user.getBalance());

        // Chỉ truyền tin nhắn và bối cảnh tài chính
        String aiReply = aiService.getFinancialAdvice(message, context);

        return ResponseEntity.ok(Map.of("reply", aiReply));
    }
}