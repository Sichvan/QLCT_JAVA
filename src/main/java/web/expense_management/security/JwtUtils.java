package web.expense_management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import web.expense_management.models.User;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Lấy chuỗi bí mật từ file application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Lấy thời gian hết hạn từ file application.properties
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // Hàm tạo Token (Giống hàm generateToken trong Node.js)
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // Biến chuỗi bí mật thành dạng Key bảo mật
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(user.getId()) // Lưu ID user vào token
                .claim("role", user.getRole()) // Lưu thêm role để check quyền Admin
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Hàm bóc tách ID của User từ chuỗi Token
    public String getUserIdFromJwtToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Hàm kiểm tra Token có hợp lệ không
    public boolean validateJwtToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi xác thực Token: " + e.getMessage());
        }
        return false;
    }
}