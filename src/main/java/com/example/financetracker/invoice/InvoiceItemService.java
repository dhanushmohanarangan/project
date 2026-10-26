package com.example.financetracker.invoice;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;

    public InvoiceItemService(InvoiceItemRepository invoiceItemRepository){
        this.invoiceItemRepository = invoiceItemRepository;
    }

    public InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem){
        return invoiceItemRepository.save(invoiceItem);
    }

    public List<InvoiceItem> getAllInvoiceItems(){
        return invoiceItemRepository.findAll();
    }

    public Optional<InvoiceItem> getInvoiceItemById(long invoiceItemId){
        return invoiceItemRepository.findById(invoiceItemId);
    }

    public void deleteInvoiceItem(long invoiceItemId){
         invoiceItemRepository.deleteById(invoiceItemId);
    }
}
