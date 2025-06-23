package net.demo.backendservice.services.impl;

import net.demo.backendservice.dao.CategoryRepository;
import net.demo.backendservice.dao.ProductRepository;
import net.demo.backendservice.dtos.product.ProductRequest;
import net.demo.backendservice.dtos.product.ProductResponse;
import net.demo.backendservice.entities.Product;
import net.demo.backendservice.exceptions.ResourceAlreadyExists;
import net.demo.backendservice.exceptions.ResourceNotFoundException;
import net.demo.backendservice.mappers.ProductMapper;
import net.demo.backendservice.services.ProductService;
import net.demo.backendservice.utils.ProductSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    // method to fetch data with pagination, sorting, and dynamic filtering (data can be fetched with and without filtering)
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProducts(String product, Double price, String category, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.filterWithoutConditions()
                .and(ProductSpecification.nameLike(product))
                .and(ProductSpecification.priceEqual(price))
                .and(ProductSpecification.categoryEqual(category));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());

        List<ProductResponse> products = productRepository.findAll(spec, pageable)
                .stream()
                .map(productMapper::productToProductResponse)
                .toList();

        return new PageImpl<>(products);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findProductById(Integer id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product with id %s not found",id)));

        return productMapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse saveNewProduct(Integer categoryId, ProductRequest request) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Category with id %s not found",categoryId)));
        productRepository.findProductByNameIgnoreCase(request.name())
                .ifPresent(product -> {
                    throw new ResourceAlreadyExists(format("Product with name %s already exists",request.name()));
                });

        var product = productMapper.productToProductRequest(request);
        product.setCategory(category);
        productRepository.save(product);
        log.info("Saved new product");

        return productMapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product with id %s not found",id)));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        productRepository.save(product);
        log.info("Updated product");

        return productMapper.productToProductResponse(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product with id %s not found",id)));

        productRepository.delete(product);
        log.info("Deleted product");
    }
}
