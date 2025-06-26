package net.demo.backendservice.controllers;

import jakarta.validation.Valid;
import net.demo.backendservice.dtos.product.ProductRequest;
import net.demo.backendservice.dtos.product.ProductResponse;
import net.demo.backendservice.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponse> getAllProducts(
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return productService.findAllProducts(product, price, category, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        ProductResponse response = productService.findProductById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestParam Integer categoryId, @RequestBody @Valid ProductRequest productRequest) {
        ProductResponse response = productService.saveNewProduct(categoryId, productRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> editProduct(@PathVariable Integer id, @RequestBody @Valid ProductRequest productRequest) {
        ProductResponse response = productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
