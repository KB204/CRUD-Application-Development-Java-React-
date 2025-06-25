package net.demo.backendservice.mappers;


import net.demo.backendservice.dtos.invoice.InvoiceRequest;
import net.demo.backendservice.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "invoices",ignore = true)
    @Mapping(target = "id",ignore = true)
    Customer requestToCustomer(InvoiceRequest request);
}
