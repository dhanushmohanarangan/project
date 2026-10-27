package com.example.financetracker.excel;
import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseService;
import com.example.financetracker.pdf.PdfService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ExcelService {

    private final ExpenseService expenseService;
    private final PdfService pdfService;

    public ExcelService(ExpenseService expenseService, PdfService pdfService) {
        this.expenseService = expenseService;
        this.pdfService = pdfService;
    }

    public int importExpenses(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select an Excel file.");
        }

        List<Expense> expenses = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row header = sheet.getRow(0);

            if (header == null) {
                throw new IllegalArgumentException("The Excel file has no header row.");
            }

            Map<String, Integer> columns = new HashMap<>();
            for (Cell cell : header) {
                String name = normalize(formatter.formatCellValue(cell));
                columns.put(name, cell.getColumnIndex());
            }

            int titleColumn = requiredColumn(columns, "title");
            int amountColumn = requiredColumn(columns, "amount");
            int categoryColumn = requiredColumn(columns, "category");

            Integer dateColumn = columns.get("expensedate");
            Integer descriptionColumn = columns.get("description");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String title = readCell(row, titleColumn, formatter);
                String amountText = readCell(row, amountColumn, formatter);
                String category = readCell(row, categoryColumn, formatter);

                if (title.isBlank() && amountText.isBlank() && category.isBlank()) {
                    continue;
                }

                if (title.isBlank() || amountText.isBlank() || category.isBlank()) {
                    throw new IllegalArgumentException("Missing title, amount, or category in Excel row " + (i + 1));
                }

                long amount;
                try {
                    String cleanAmount = amountText.replaceAll("[^0-9.]", "").trim();
                    if (cleanAmount.contains(".")) {
                        cleanAmount = cleanAmount.split("\\.")[0];
                    }
                    if (cleanAmount.isEmpty()) {
                        throw new IllegalArgumentException("Amount cell contains no numeric digits");
                    }
                    amount = Long.parseLong(cleanAmount);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Invalid whole-number amount ('" + amountText + "') in Excel row " + (i + 1)
                    );
                }

                if (amount < 0) {
                    throw new IllegalArgumentException("Amount cannot be negative in Excel row " + (i + 1));
                }

                Expense expense = new Expense();
                expense.setTitle(title);
                expense.setAmount(amount);
                expense.setCategory(category);


                LocalDate date = LocalDate.now();
                if (dateColumn != null) {
                    Cell dateCell = row.getCell(dateColumn);
                    if (dateCell != null && dateCell.getCellType() != CellType.BLANK) {


                        if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                            Date rawDate = dateCell.getDateCellValue();
                            if (rawDate != null) {
                                date = rawDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            }
                        } else {
                            String dateText = formatter.formatCellValue(dateCell).trim();
                            if (!dateText.isBlank()) {
                                try {
                                    date = LocalDate.parse(dateText);
                                } catch (Exception e) {
                                    date = LocalDate.now();
                                }
                            }
                        }
                    }
                }
                expense.setExpenseDate(date);

                if (descriptionColumn != null) {
                    expense.setDescription(readCell(row, descriptionColumn, formatter));
                }

                expenses.add(expense);
            }

            int savedCount = 0;
            for (Expense exp : expenses) {
                expenseService.addExpense(exp);
                savedCount++;
            }

            return savedCount;
        }
    }

    public byte[] exportExpenses() throws IOException {
        List<Expense> expenses = expenseService.getAllExpenses();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Expenses");
            Row headerRow = sheet.createRow(0);

            String[] headers = {"Title", "Amount", "Category", "Expense Date", "Description"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Expense expense : expenses) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(expense.getTitle());
                row.createCell(1).setCellValue(expense.getAmount());
                row.createCell(2).setCellValue(expense.getCategory());
                row.createCell(3).setCellValue(expense.getExpenseDate().toString());
                row.createCell(4).setCellValue(expense.getDescription());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] convertExcelToPdf(MultipartFile file) throws IOException {
        return pdfService.generatePdfFromExcel(file.getInputStream());
    }

    private String normalize(String value) {
        return value.toLowerCase().replaceAll("[\\s_-]", "");
    }

    private int requiredColumn(Map<String, Integer> columns, String name) {
        Integer index = columns.get(name);
        if (index == null) {
            throw new IllegalArgumentException("Missing required column: " + name);
        }
        return index;
    }

    private String readCell(Row row, int columnIndex, DataFormatter formatter) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }
        return formatter.formatCellValue(cell);
    }
}
