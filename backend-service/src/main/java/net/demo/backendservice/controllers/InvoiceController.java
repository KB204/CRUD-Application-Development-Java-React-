package net.demo.backendservice.controllers;


import jakarta.validation.Valid;
import net.demo.backendservice.dtos.invoice.InvoiceRequest;
import net.demo.backendservice.dtos.invoice.InvoiceResponse;
import net.demo.backendservice.dtos.invoice.InvoiceResponseDto;
import net.demo.backendservice.services.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.lang.String.*;

@RestController
@RequestMapping("/v1/api/invoices")
@CrossOrigin(origins = "http://localhost:3000")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<InvoiceResponse> getAllInvoices(
            @RequestParam(required = false) String invoiceIdentifier,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String customerEmail,
            Pageable pageable) {
        return invoiceService.findAllInvoices(invoiceIdentifier, date, status, price, customerEmail, pageable);
    }

    @GetMapping("/{invoiceIdentifier}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByIdentifier(@PathVariable String invoiceIdentifier) {
        InvoiceResponseDto response = invoiceService.findInvoiceDetails(invoiceIdentifier);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody @Valid InvoiceRequest request) {
        InvoiceResponse response = invoiceService.saveNewInvoice(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PatchMapping("/{invoiceIdentifier}/cancel")
    public ResponseEntity<String> cancelInvoice(@PathVariable String invoiceIdentifier) {
        invoiceService.cancelInvoice(invoiceIdentifier);
        return new ResponseEntity<>(format("Invoice identified by %s was canceled",invoiceIdentifier),HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{invoiceIdentifier}/complete")
    public ResponseEntity<String> completeInvoice(@PathVariable String invoiceIdentifier) {
        invoiceService.completeInvoice(invoiceIdentifier);
        return new ResponseEntity<>(String.format("Invoice identified by %s was completed",invoiceIdentifier),HttpStatus.ACCEPTED);
    }
}
