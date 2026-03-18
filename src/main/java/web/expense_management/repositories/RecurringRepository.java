package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Recurring;
import java.util.List;

public interface RecurringRepository extends MongoRepository<Recurring, String> {
    List<Recurring> findByUserOrderByDayOfMonthAsc(String user);
    List<Recurring> findByDayOfMonthAndIsActiveTrue(int dayOfMonth); // Dùng cho Scheduler
}