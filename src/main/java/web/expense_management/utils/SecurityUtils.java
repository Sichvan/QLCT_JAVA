package web.expense_management.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import web.expense_management.models.User;

public class SecurityUtils {

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Unauthorized: No user logged in");
        }
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }
}
