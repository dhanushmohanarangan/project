package com.example.financetracker.invoice;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController{

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public Invoice saveInvoice(@RequestBody Invoice invoice){
        return invoiceService.saveInvoice(invoice);
    }

    @GetMapping
    public List<Invoice> getAllInvoice(){
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public Optional<Invoice> getInvoiceById(@PathVariable long id){
        return invoiceService.getInvoiceById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable long id){
         invoiceService.deleteInvoice(id);
    }
}
