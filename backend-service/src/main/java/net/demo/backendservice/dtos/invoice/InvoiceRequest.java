package net.demo.backendservice.dtos.invoice;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.demo.backendservice.dtos.customer.CustomerResponse;

import java.util.List;

public record InvoiceRequest(
        @NotEmpty(message = "FirstName is required")
        String firstName,
        @NotEmpty(message = "LastName is required")
        String lastName,
        @NotEmpty(message = "Email is required")
        @Email(message = "Please enter a valid email")
        String email,
        @NotEmpty(message = "Phone number is required")
        @Size(min = 10,max = 10,message = "Please enter a valid phone number")
        String phone,
        String address,
        @NotNull(message = "The product is required")
        List<Integer> productId) {}
