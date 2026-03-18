package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Income;
import java.util.List;

public interface IncomeRepository extends MongoRepository<Income, String> {
    List<Income> findByUserOrderByDateDesc(String user);
    // Tìm kiếm trong khoảng thời gian
    List<Income> findByUserAndDateBetween(String user, java.util.Date startDate, java.util.Date endDate);
}