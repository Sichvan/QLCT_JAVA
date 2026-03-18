package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByUser(String user);
    // Tìm xem user đã tạo ngân sách cho danh mục này chưa
    Optional<Budget> findByUserAndCategory(String user, String category); 
}