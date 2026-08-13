package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.CategoryRequest;
import web.expense_management.models.Category;
import web.expense_management.repositories.CategoryRepository;
import web.expense_management.utils.SecurityUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findByUserOrUserIsNull(SecurityUtils.getCurrentUserId());
    }

    public Category createCategory(CategoryRequest request) {
        if (request.getName() == null || request.getType() == null || request.getIcon() == null) {
            throw new RuntimeException("Thiếu thông tin");
        }

        Category category = new Category();
        category.setUser(SecurityUtils.getCurrentUserId());
        category.setName(request.getName());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setDefault(false);

        return categoryRepository.save(category);
    }

    public Category updateCategory(String id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy"));
        
        if (category.getUser() == null || !category.getUser().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Không thể sửa danh mục mặc định hoặc của người khác");
        }

        if (request.getName() != null) category.setName(request.getName());
        if (request.getIcon() != null) category.setIcon(request.getIcon());

        return categoryRepository.save(category);
    }

    public Map<String, String> deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy"));
        
        if (category.getUser() == null || !category.getUser().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Không thể xóa danh mục mặc định hoặc của người khác");
        }

        categoryRepository.delete(category);
        return Map.of("message", "Đã xóa");
    }
}
