package com.example.financetracker.invoice;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoiceItemId;
    private long invoiceId;
    private String itemName;
    private long quantity;
    private long unitPrice;
    private long lineTotal;

    public InvoiceItem() {

    }

    public InvoiceItem(long invoiceItemId, long invoiceId, String itemName, long quantity, long unitPrice, long lineTotal) {
        this.invoiceItemId = invoiceItemId;
        this.invoiceId = invoiceId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public long getInvoiceItemId() {
        return invoiceItemId;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public String getItemName() {
        return itemName;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public long getLineTotal() {
        return lineTotal;
    }

    public void setInvoiceItemId(long invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setLineTotal(long lineTotal) {
        this.lineTotal = lineTotal;
    }
}
