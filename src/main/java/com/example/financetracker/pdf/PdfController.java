package com.example.financetracker.pdf;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadAllExpenses() throws IOException {

        byte[] pdf = pdfService.createExpenseReport();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=expenses.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/daily/download")
    public ResponseEntity<ByteArrayResource> downloadDailyReport(
            @RequestParam LocalDate date) throws IOException {

        Path path = pdfService.getDailyReportPath(date);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdf = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + path.getFileName()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }
}