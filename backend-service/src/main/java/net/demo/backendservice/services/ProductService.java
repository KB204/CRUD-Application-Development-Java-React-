package net.demo.backendservice.services;

import net.demo.backendservice.dtos.product.ProductRequest;
import net.demo.backendservice.dtos.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResponse> findAllProducts(String product, Double price, String category, Pageable pageable);
    ProductResponse findProductById(Integer id);
    ProductResponse saveNewProduct(Integer categoryId, ProductRequest request);
    ProductResponse updateProduct(Integer id, ProductRequest request);
    void deleteProduct(Integer id);
}
