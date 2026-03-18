package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Expense;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserOrderByDateDesc(String user);
    // Lấy danh sách chi tiêu của một user, thuộc một category, trong một khoảng thời gian
    List<Expense> findByUserAndCategoryAndDateBetween(String user, String category, java.util.Date startDate, java.util.Date endDate);
    // Tìm kiếm trong khoảng thời gian
    List<Expense> findByUserAndDateBetween(String user, java.util.Date startDate, java.util.Date endDate);
}