package com.example.financetracker.scheduler;
import com.example.financetracker.pdf.PdfService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class ExpenseJob implements Job {

    @Autowired
    private PdfService pdfService;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            pdfService.generateDailyReport();
        } catch (Exception e) {
            throw new JobExecutionException(
                    "Could not generate daily expense report.",
                    e
            );
        }
    }
}