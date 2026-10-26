package com.example.financetracker.invoice;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoice-items")
public class InvoiceItemController{

    private final InvoiceItemService invoiceItemService;

    public InvoiceItemController(InvoiceItemService invoiceItemService){
        this.invoiceItemService = invoiceItemService;
    }

    @PostMapping
    public InvoiceItem saveInvoiceItem(@RequestBody InvoiceItem invoiceItem){
        return invoiceItemService.saveInvoiceItem(invoiceItem);
    }

    @GetMapping
    public List<InvoiceItem> getAllInvoiceItems(){
        return invoiceItemService.getAllInvoiceItems();
    }

    @GetMapping("/{id}")
    public Optional<InvoiceItem> getInvoiceItemById(@PathVariable long id){
        return invoiceItemService.getInvoiceItemById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoiceItem(@PathVariable long id){
        invoiceItemService.deleteInvoiceItem(id);
    }
}
