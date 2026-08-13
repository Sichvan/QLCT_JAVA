package web.expense_management.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.dtos.CategoryRequest;
import web.expense_management.models.Category;
import web.expense_management.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        log.info("Lấy danh sách danh mục");
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        log.info("Tạo danh mục mới: {}", request.getName());
        return ResponseEntity.status(201).body(categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        log.info("Cập nhật danh mục id: {}", id);
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        log.info("Xóa danh mục id: {}", id);
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}