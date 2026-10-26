package com.example.financetracker.excel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Service
public class ExcelService{

    public String readExcel(MultipartFile file) throws Exception{

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for(int i = 1; i <= sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);

            if(row != null){
                for(Cell cell : row){
                    System.out.print(cell);
                }
                System.out.println();
            }
        }
        workbook.close();
        return "Excel file read!";
    }

    public byte[] createExcel() throws Exception{

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("expenses");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Title");
        header.createCell(1).setCellValue("Amount");
        header.createCell(2).setCellValue("Category");

        Row row = sheet.createRow(0);
        header.createCell(0).setCellValue("Office supplies");
        header.createCell(1).setCellValue(1500);
        header.createCell(2).setCellValue("Office");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        workbook.write(output);
        workbook.close();
        return output.toByteArray();
    }
}
