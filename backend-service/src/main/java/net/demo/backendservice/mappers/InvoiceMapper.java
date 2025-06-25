package net.demo.backendservice.mappers;

import net.demo.backendservice.dtos.invoice.InvoiceResponse;
import net.demo.backendservice.dtos.invoice.InvoiceResponseDto;
import net.demo.backendservice.entities.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceResponse invoiceToInvoiceResponse(Invoice invoice);
    InvoiceResponseDto invoiceToInvoiceResponseDto(Invoice invoice);
}
