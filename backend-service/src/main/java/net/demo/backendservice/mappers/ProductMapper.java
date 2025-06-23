package net.demo.backendservice.mappers;

import net.demo.backendservice.dtos.product.ProductRequest;
import net.demo.backendservice.dtos.product.ProductResponse;
import net.demo.backendservice.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse productToProductResponse(Product product);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product productToProductRequest(ProductRequest productRequest);
}
