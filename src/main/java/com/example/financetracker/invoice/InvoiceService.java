package com.example.financetracker.invoice;

import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository){
        this.invoiceRepository =  invoiceRepository;
    }

    public Invoice saveInvoice(Invoice invoice){
        return invoiceRepository.save(invoice);
    }

    public Optional<Invoice> getInvoiceById(long invoiceId){
        return invoiceRepository.findById(invoiceId);
    }

    public List<Invoice> getAllInvoices(){
        return invoiceRepository.findAll();
    }

    public void deleteInvoice(long invoiceId){
        invoiceRepository.deleteById(invoiceId);
    }
}
