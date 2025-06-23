package net.demo.backendservice.dtos.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotEmpty(message = "Product's Name is required")
        String name,
        String description,
        @NotNull(message = "Product's Price is required")
        @Min(value = 1,message = "Product's Price can't be less than 1")
        Double price) {}
