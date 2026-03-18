package web.expense_management.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import web.expense_management.models.User;
import java.util.Optional;

// MongoRepository<Tên_Model, Kiểu_dữ_liệu_của_ID>
public interface UserRepository extends MongoRepository<User, String> {
    
    // Spring Data tự động sinh ra câu query tìm user theo username (email)!
    // Tương đương: User.findOne({ username: username }) bên Node.js
    Optional<User> findByUsername(String username);
    
    // Kiểm tra xem email đã tồn tại chưa (dùng lúc đăng ký)
    boolean existsByUsername(String username);
}