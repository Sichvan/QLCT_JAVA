package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Category;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    // Câu lệnh này tự động tìm: Các danh mục của user truyền vào HOẶC danh mục có user = null
    // Tương đương logic: { $or: [{ user: req.user.id }, { user: null }] }
    List<Category> findByUserOrUserIsNull(String userId);
}