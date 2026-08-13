package web.expense_management.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.services.StatsService;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/chart-data")
    public ResponseEntity<?> getChartData(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("Lấy dữ liệu chart tổng quan: start={}, end={}", startDate, endDate);
        return ResponseEntity.ok(statsService.getChartData(startDate, endDate));
    }

    @GetMapping("/pie-chart")
    public ResponseEntity<?> getPieChart(
            @RequestParam String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("Lấy dữ liệu pie chart: type={}, start={}, end={}", type, startDate, endDate);
        return ResponseEntity.ok(statsService.getPieChart(type, startDate, endDate));
    }
}