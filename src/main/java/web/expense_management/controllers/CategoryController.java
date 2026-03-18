package web.expense_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.CategoryRequest;
import web.expense_management.models.Category;
import web.expense_management.models.User;
import web.expense_management.repositories.CategoryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Hàm phụ trợ để lấy ID của User đang gửi request (bóc từ Token JWT ra)
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    // 1. LẤY DANH SÁCH DANH MỤC
    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        String userId = getCurrentUserId();
        List<Category> categories = categoryRepository.findByUserOrUserIsNull(userId);
        
        // Bạn có thể thêm logic sắp xếp ưu tiên isDefault lên đầu ở đây nếu muốn
        return ResponseEntity.ok(categories);
    }

    // 2. THÊM DANH MỤC (Chỉ user tự tạo)
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        if (request.getName() == null || request.getType() == null || request.getIcon() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thiếu thông tin"));
        }

        Category category = new Category();
        category.setUser(getCurrentUserId()); // Luôn gắn với ID người tạo
        category.setName(request.getName());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setDefault(false); // Danh mục cá nhân luôn là false

        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(201).body(savedCategory);
    }

    // 3. SỬA DANH MỤC
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        Optional<Category> catOpt = categoryRepository.findById(id);
        if (catOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy"));
        }

        Category category = catOpt.get();
        String currentUserId = getCurrentUserId();

        // BẢO MẬT: Chặn sửa nếu là danh mục chung (user = null) hoặc không phải chủ sở hữu
        if (category.getUser() == null || !category.getUser().equals(currentUserId)) {
            return ResponseEntity.status(403).body(Map.of("message", "Không thể sửa danh mục mặc định hoặc của người khác"));
        }

        if (request.getName() != null) category.setName(request.getName());
        if (request.getIcon() != null) category.setIcon(request.getIcon());

        Category updatedCat = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCat);
    }

    // 4. XÓA DANH MỤC
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        Optional<Category> catOpt = categoryRepository.findById(id);
        if (catOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy"));
        }

        Category category = catOpt.get();
        String currentUserId = getCurrentUserId();

        // BẢO MẬT: Chặn xóa tương tự như khi sửa
        if (category.getUser() == null || !category.getUser().equals(currentUserId)) {
            return ResponseEntity.status(403).body(Map.of("message", "Không thể xóa danh mục mặc định hoặc của người khác"));
        }

        categoryRepository.delete(category);
        return ResponseEntity.ok(Map.of("message", "Đã xóa"));
    }
}