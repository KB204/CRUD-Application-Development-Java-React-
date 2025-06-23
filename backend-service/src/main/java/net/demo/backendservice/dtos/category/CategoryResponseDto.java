package net.demo.backendservice.dtos.category;

import net.demo.backendservice.dtos.product.ProductResponseDto;

import java.util.List;

public record CategoryResponseDto(Integer id, String name, String description, List<ProductResponseDto> products) {}
