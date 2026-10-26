package com.example.financetracker.pdf;
import com.example.financetracker.expense.Expense;
import com.example.financetracker.expense.ExpenseRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
@Service
public class PdfService {
    private final ExpenseRepository repository;
    public PdfService(ExpenseRepository repository) {
        this.repository = repository;
    }
    public byte[] createPdf() throws IOException {
        List<Expense> expenses = repository.findAll();

        PDDocument document = new PDDocument();

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PDPage page = new PDPage(); document.addPage(page);

        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        PDPageContentStream content = new PDPageContentStream(document, page);

        content.beginText(); content.setFont(font, 16);

        content.newLineAtOffset(50, 750);

        content.showText("Finance Tracker - Expense Report");

        content.setFont(font, 10);

        content.newLineAtOffset(0, -30);

        content.showText("Total Records: " + expenses.size());

        for (Expense expense : expenses) {

            content.newLineAtOffset(0, -20);
            String line = expense.getTitle() + " | Rs. " + expense.getAmount() + " | " + expense.getCategory() + " | " + expense.getExpenseDate(); content.showText(line);
        }
        content.endText();
        content.close();
        document.save(output);
        document.close();
        return output.toByteArray();
    }
}