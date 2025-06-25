package net.demo.backendservice.dtos.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.demo.backendservice.dtos.customer.CustomerResponse;
import net.demo.backendservice.dtos.product.ProductResponseDto;
import net.demo.backendservice.enums.InvoiceStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record InvoiceResponseDto(
        String invoiceIdentifier,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime invoiceDate,
        InvoiceStatus invoiceStatus,
        Double totalPrice,
        CustomerResponse customer,
        Set<ProductResponseDto> products) {}
