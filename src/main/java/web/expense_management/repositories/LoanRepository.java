package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.Loan;
import java.util.List;

public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByUserOrderByDateDesc(String user);
    // Tìm kiếm trong khoảng thời gian
    List<Loan> findByUserAndDateBetween(String user, java.util.Date startDate, java.util.Date endDate);
}