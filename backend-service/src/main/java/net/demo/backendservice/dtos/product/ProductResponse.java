package net.demo.backendservice.dtos.product;

import net.demo.backendservice.dtos.category.CategoryResponse;

public record ProductResponse(Integer id, String name, String description, Double price, CategoryResponse category) {}
