package com.example.financetracker.excel;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class ExcelController{

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService){
        this.excelService = excelService;
    }
    @PostMapping("/read")
    public String readExcel(@RequestParam("file")MultipartFile file) throws Exception{
        return excelService.readExcel(file);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel() throws Exception{
        byte[] file = excelService.createExcel();

        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = expenses.xlsx").
                contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheethtml.sheet"))
                .body(file);
    }
}
