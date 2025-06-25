package net.demo.backendservice.services;

import net.demo.backendservice.dtos.invoice.InvoiceRequest;
import net.demo.backendservice.dtos.invoice.InvoiceResponse;
import net.demo.backendservice.dtos.invoice.InvoiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceService {
    Page<InvoiceResponse> findAllInvoices(String invoiceIdentifier, String date, String status, Double price, String customerEmail, Pageable pageable);
    InvoiceResponseDto findInvoiceDetails(String invoiceIdentifier);
    InvoiceResponse saveNewInvoice(InvoiceRequest request);
    void cancelInvoice(String invoiceIdentifier);
    void completeInvoice(String invoiceIdentifier);
}
