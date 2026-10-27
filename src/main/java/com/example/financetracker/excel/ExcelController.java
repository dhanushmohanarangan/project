package com.example.financetracker.excel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/api/excel")
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/expenses/import")
    public String importExpenses(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            int count = excelService.importExpenses(file);

            redirectAttributes.addFlashAttribute(
                    "message",
                    count + " expenses imported successfully."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Import failed: " + e.getMessage()
            );
        }

        return "redirect:/dashboard";
    }


    @GetMapping("/expenses/export")
    public ResponseEntity<byte[]> exportExpenses() throws IOException {

        byte[] excel = excelService.exportExpenses();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=expenses.xlsx"
                )
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(excel);
    }


    @PostMapping("/convert-to-pdf")
    public ResponseEntity<byte[]> convertExcelToPdf(
            @RequestParam("file") MultipartFile file) throws IOException {

        byte[] pdf = excelService.convertExcelToPdf(file);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=converted-excel.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}