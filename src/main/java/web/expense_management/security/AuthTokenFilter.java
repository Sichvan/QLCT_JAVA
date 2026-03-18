package web.expense_management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import web.expense_management.models.User;
import web.expense_management.repositories.UserRepository;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Lấy token từ header
            String jwt = parseJwt(request);
            
            // 2. Nếu có token và token hợp lệ
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Lấy ID user từ token
                String userId = jwtUtils.getUserIdFromJwtToken(jwt);

                // 3. Tìm user trong Database (Tương đương req.user = await User.findById(decoded.id))
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    // 4. Tạo "giấy thông hành" báo cho Spring Security biết user này đã hợp lệ
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            System.out.println("Không thể thiết lập xác thực người dùng: " + e.getMessage());
        }

        // 5. Cho phép đi tiếp đến Controller
        filterChain.doFilter(request, response);
    }

    // Hàm phụ trợ bóc chữ "Bearer " ra khỏi token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}