package net.demo.backendservice.dtos.category;

import jakarta.validation.constraints.NotEmpty;


public record CategoryRequest(@NotEmpty(message = "Name of the category is required") String name, String description) {}
