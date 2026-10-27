package com.example.financetracker.pdf;
import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    private final ExpenseService expenseService;

    public PdfService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public byte[] generatePdfFromExcel(InputStream excelInputStream) throws IOException {
        List<String[]> tableData = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = new XSSFWorkbook(excelInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            if (rowCount > 0) {
                Row headerRow = sheet.getRow(0);
                int columnCount = headerRow.getPhysicalNumberOfCells();

                for (int i = 0; i < rowCount; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String[] rowData = new String[columnCount];
                    for (int j = 0; j < columnCount; j++) {
                        Cell cell = row.getCell(j);
                        rowData[j] = formatter.formatCellValue(cell);
                    }
                    tableData.add(rowData);
                }
            }
        }

        String[] headers = tableData.isEmpty() ? new String[]{"Data"} : tableData.remove(0);
        return drawPdfGridTable("Converted Excel Report", headers, tableData);
    }

    public byte[] createExpenseReport() throws IOException {
        return createExpenseReport(
                expenseService.getAllExpenses(),
                "All Expenses Report"
        );
    }

    public byte[] createExpenseReport(List<Expense> expenses, String title) throws IOException {
        String[] headers = {"ID", "Title", "Amount", "Category", "Date", "Description"};
        List<String[]> rows = new ArrayList<>();

        for (Expense expense : expenses) {
            rows.add(new String[]{
                    String.valueOf(expense.getExpenseId()),
                    safe(expense.getTitle()),
                    String.valueOf(expense.getAmount()),
                    safe(expense.getCategory()),
                    expense.getExpenseDate() != null ? expense.getExpenseDate().toString() : "",
                    safe(expense.getDescription()) // Included missing description parameter field
            });
        }

        return drawPdfGridTable(title, headers, rows);
    }

    private byte[] drawPdfGridTable(String title, String[] headers, List<String[]> contentRows) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            int totalColumns = headers.length;
            float margin = 40;
            float tableWidth = 530;
            float yStart = 730;
            float rowHeight = 22;
            float cellPadding = 5;

            float colWidth = tableWidth / (float) totalColumns;

            int rowIndex = 0;
            boolean isFirstPage = true;

            while (isFirstPage || rowIndex < contentRows.size()) {
                isFirstPage = false;
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                    float currentY = yStart;

                    stream.beginText();
                    stream.setFont(boldFont, 16);
                    stream.newLineAtOffset(margin, currentY);
                    stream.showText(cleanText(title));
                    stream.endText();

                    currentY -= 30;

                    stream.setFont(boldFont, 10);
                    float nextX = margin;
                    for (String header : headers) {
                        stream.setNonStrokingColor(230 / 255f, 235 / 255f, 240 / 255f);
                        stream.addRect(nextX, currentY - rowHeight, colWidth, rowHeight);
                        stream.fill();
                        stream.setNonStrokingColor(0, 0, 0);

                        stream.setLineWidth(0.5f);
                        stream.addRect(nextX, currentY - rowHeight, colWidth, rowHeight);
                        stream.stroke();

                        stream.beginText();
                        stream.newLineAtOffset(nextX + cellPadding, currentY - rowHeight + cellPadding + 3);
                        stream.showText(cleanText(header));
                        stream.endText();

                        nextX += colWidth;
                    }
                    currentY -= rowHeight;

                    stream.setFont(normalFont, 9);
                    while (rowIndex < contentRows.size() && currentY > 60) {
                        String[] rowData = contentRows.get(rowIndex);
                        nextX = margin;

                        for (int col = 0; col < totalColumns; col++) {
                            String cellValue = col < rowData.length ? rowData[col] : "";

                            stream.addRect(nextX, currentY - rowHeight, colWidth, rowHeight);
                            stream.stroke();

                            stream.beginText();
                            stream.newLineAtOffset(nextX + cellPadding, currentY - rowHeight + cellPadding + 3);
                            stream.showText(cleanText(cellValue));
                            stream.endText();

                            nextX += colWidth;
                        }
                        currentY -= rowHeight;
                        rowIndex++;
                    }
                }
            }

            document.save(output);
            return output.toByteArray();
        }
    }

    @Deprecated
    public byte[] createTextPdf(String title, List<String> lines) throws IOException {
        List<String[]> dataRows = new ArrayList<>();
        for (String line : lines) {
            dataRows.add(line.split("\\s*\\|\\s*"));
        }
        String[] mockHeaders = dataRows.isEmpty() ? new String[]{"Report"} : dataRows.remove(0);
        return drawPdfGridTable(title, mockHeaders, dataRows);
    }

    public Path generateDailyReport() throws IOException {
        LocalDate today = LocalDate.now();
        List<Expense> expenses = expenseService.getExpensesByDate(today);

        byte[] pdf = createExpenseReport(expenses, "Daily Expense Report - " + today);

        Path folder = Path.of("reports");
        Files.createDirectories(folder);
        Path file = folder.resolve("expense-report-" + today + ".pdf");
        Files.write(file, pdf);

        return file;
    }

    public Path getDailyReportPath(LocalDate date) {
        return Path.of("reports", "expense-report-" + date + ".pdf");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String cleanText(String value) {
        if (value == null) {
            return "";
        }
        String cleaned = value.replaceAll("[^\\x20-\\x7E]", "?");

        if (cleaned.length() > 20) {
            cleaned = cleaned.substring(0, 17) + "...";
        }
        return cleaned;
    }
}
