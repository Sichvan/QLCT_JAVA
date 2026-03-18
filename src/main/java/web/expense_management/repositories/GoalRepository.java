package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Goal;
import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, String> {
    // Tự động tìm tất cả mục tiêu của một user và sắp xếp mới nhất lên đầu
    List<Goal> findByUserOrderByCreatedAtDesc(String userId);
}