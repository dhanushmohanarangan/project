package com.example.financetracker.invoice;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoiceId;
    private String invoiceNumber;
    private String customerNumber;
    private LocalDate invoiceDate;
    private Long totalAmount;
    private String status;

    public Invoice() {

    }

    public Invoice(long invoiceId, String invoiceNumber, String customerNumber, LocalDate invoiceDate, Long totalAmount, String status) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.customerNumber = customerNumber;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
