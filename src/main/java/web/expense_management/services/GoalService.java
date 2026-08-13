package web.expense_management.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.expense_management.dtos.DepositRequest;
import web.expense_management.dtos.GoalRequest;
import web.expense_management.models.Goal;
import web.expense_management.models.User;
import web.expense_management.repositories.GoalRepository;
import web.expense_management.repositories.UserRepository;
import web.expense_management.utils.SecurityUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public List<Goal> getGoals() {
        return goalRepository.findByUserOrderByCreatedAtDesc(SecurityUtils.getCurrentUserId());
    }

    public Goal createGoal(GoalRequest request) {
        Goal goal = new Goal();
        goal.setUser(SecurityUtils.getCurrentUserId());
        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setDeadline(request.getDeadline());

        return goalRepository.save(goal);
    }

    public Goal updateGoal(String id, GoalRequest request) {
        Optional<Goal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty() || !goalOpt.get().getUser().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Không tìm thấy mục tiêu");
        }
        Goal goal = goalOpt.get();
        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        return goalRepository.save(goal);
    }

    public Map<String, Object> depositToGoal(String id, DepositRequest request) {
        if (request.getAmount() <= 0) {
            throw new RuntimeException("Số tiền không hợp lệ");
        }

        Optional<Goal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty() || !goalOpt.get().getUser().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Không tìm thấy mục tiêu");
        }

        User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow();
        Goal goal = goalOpt.get();

        if (user.getBalance() < request.getAmount()) {
            throw new RuntimeException("Số dư ví không đủ để tiết kiệm");
        }

        user.setBalance(user.getBalance() - request.getAmount());
        goal.setCurrentAmount(goal.getCurrentAmount() + request.getAmount());

        boolean isSuccess = false;
        if (goal.getCurrentAmount() >= goal.getTargetAmount()) {
            goal.setStatus("completed");
            isSuccess = true;
        }

        userRepository.save(user);
        goalRepository.save(goal);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Nạp tiền thành công");
        response.put("updatedGoal", goal);
        response.put("newBalance", user.getBalance());
        response.put("isSuccess", isSuccess);
        return response;
    }

    public Map<String, String> deleteGoal(String id) {
        Optional<Goal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty() || !goalOpt.get().getUser().equals(SecurityUtils.getCurrentUserId())) {
            throw new RuntimeException("Không tìm thấy mục tiêu");
        }

        Goal goal = goalOpt.get();
        String message = "Đã xóa mục tiêu thành công";

        if ("ongoing".equals(goal.getStatus()) && goal.getCurrentAmount() > 0 && goal.getCurrentAmount() < goal.getTargetAmount()) {
            User user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow();
            user.setBalance(user.getBalance() + goal.getCurrentAmount());
            userRepository.save(user);
            message = String.format("Đã xóa. Hoàn lại %,.0f đ vào ví.", goal.getCurrentAmount());
        }

        goalRepository.delete(goal);
        return Map.of("message", message);
    }
}
