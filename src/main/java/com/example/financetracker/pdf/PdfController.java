package com.example.financetracker.pdf;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
@RestController
@RequestMapping("/api/expenses/pdf")
public class PdfController {
    private final PdfService service;
    public PdfController(PdfService service){
        this.service = service;
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPdf() throws IOException {
        byte[] file = service.createPdf();
        return ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.pdf") .contentType(MediaType.APPLICATION_PDF).body(file);
    }
}

