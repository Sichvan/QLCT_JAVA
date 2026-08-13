package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.ProfileUpdateRequest;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;
import web.expense_management.utils.SecurityUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User updateProfile(ProfileUpdateRequest request) {
        User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow();

        if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepository.save(user);
    }

    public User getProfile() {
        return userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow();
    }

    public Map<String, String> uploadAvatar(String avatarData) {
        if (avatarData == null || avatarData.isEmpty()) {
            throw new RuntimeException("Thiếu dữ liệu avatar");
        }
        if (avatarData.length() > 3_000_000) {
            throw new RuntimeException("Ảnh quá lớn! Vui lòng chọn ảnh dưới 2MB");
        }
        
        User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow();
        user.setAvatarUrl(avatarData);
        userRepository.save(user);
        
        return Map.of("message", "Cập nhật avatar thành công", "avatarUrl", avatarData);
    }
}
