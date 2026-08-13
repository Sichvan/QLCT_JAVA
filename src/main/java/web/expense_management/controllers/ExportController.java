package web.expense_management.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.expense_management.services.ExportService;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String date) {
        
        log.info("Xuất file CSV: filter={}, date={}", filter, date);
        byte[] csvBytes = exportService.generateCsv(filter, date);
        String filename = "GiaoDich_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }
}
