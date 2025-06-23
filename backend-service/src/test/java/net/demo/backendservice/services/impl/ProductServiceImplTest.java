package net.demo.backendservice.services.impl;

import net.demo.backendservice.dao.CategoryRepository;
import net.demo.backendservice.dao.ProductRepository;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.dtos.product.ProductRequest;
import net.demo.backendservice.dtos.product.ProductResponse;
import net.demo.backendservice.entities.Category;
import net.demo.backendservice.entities.Product;
import net.demo.backendservice.exceptions.ResourceNotFoundException;
import net.demo.backendservice.mappers.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldFindAllProducts() {
        // given
        String productName = "test";
        Double price = 10.0;
        String categoryName = "electronics";
        Pageable pageable = PageRequest.of(0, 10);
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("price").descending());

        Category category = new Category();
        category.setName("electronics");
        category.setDescription("Electronic devices");

        List<Product> products = List.of(
                new Product(1, "test product 1", "description 1", 10.0, category,new HashSet<>()),
                new Product(2, "test product 2", "description 2", 10.0, category,new HashSet<>())
        );

        CategoryResponse categoryResponse = new CategoryResponse(1, "electronics", "Electronic devices");

        List<ProductResponse> productResponses = List.of(
                new ProductResponse(1, "test product 1", "description 1", 10.0, categoryResponse),
                new ProductResponse(2, "test product 2", "description 2", 10.0, categoryResponse)
        );

        Page<Product> productPage = new PageImpl<>(products);

        // when
        when(productRepository.findAll(any(Specification.class), eq(expectedPageable))).thenReturn(productPage);
        when(productMapper.productToProductResponse(products.get(0))).thenReturn(productResponses.get(0));
        when(productMapper.productToProductResponse(products.get(1))).thenReturn(productResponses.get(1));

        Page<ProductResponse> result = productService.findAllProducts(productName, price, categoryName, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0)).isEqualTo(productResponses.get(0));
        assertThat(result.getContent().get(1)).isEqualTo(productResponses.get(1));
    }

    @Test
    void shouldFindProductById() {
        // given
        Integer productId = 1;
        Category category = new Category();
        category.setName("electronics");
        category.setDescription("Electronic devices");

        Product product = new Product(productId, "test product", "description", 10.0, category,new HashSet<>());

        CategoryResponse categoryResponse = new CategoryResponse(1, "electronics", "Electronic devices");
        ProductResponse expectedResponse = new ProductResponse(productId, "test product", "description", 10.0, categoryResponse);

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.productToProductResponse(product)).thenReturn(expectedResponse);

        ProductResponse result = productService.findProductById(productId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // given
        Integer productId = 999;

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.findProductById(productId))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void shouldSaveNewProduct() {
        // given
        Integer categoryId = 1;
        ProductRequest request = new ProductRequest("New Product", "New Description", 15.99);

        Category category = new Category();
        category.setId(categoryId);
        category.setName("electronics");
        category.setDescription("Electronic devices");

        Product product = new Product();
        product.setName("New Product");
        product.setDescription("New Description");
        product.setPrice(15.99);

        CategoryResponse categoryResponse = new CategoryResponse(categoryId, "electronics", "Electronic devices");
        ProductResponse expectedResponse = new ProductResponse(1, "New Product", "New Description", 10.00, categoryResponse);

        // when
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findProductByNameIgnoreCase(request.name())).thenReturn(Optional.empty());
        when(productMapper.productToProductRequest(request)).thenReturn(product);
        when(productMapper.productToProductResponse(product)).thenReturn(expectedResponse);

        ProductResponse result = productService.saveNewProduct(categoryId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        verify(productRepository).save(product);
    }

    @Test
    void shouldNotCreateProductWhenCategoryNotFound() {
        // given
        Integer categoryId = 999;
        ProductRequest request = new ProductRequest("New Product", "New Description", 15.99);

        // when
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.saveNewProduct(categoryId, request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldUpdateProduct() {
        // given
        Integer productId = 1;
        ProductRequest request = new ProductRequest("Updated Product", "Updated Description", 25.99);

        Category category = new Category();
        category.setName("electronics");
        category.setDescription("Electronic devices");

        Product existingProduct = new Product(productId, "Old Product", "Old Description", 15.99, category,new HashSet<>());

        CategoryResponse categoryResponse = new CategoryResponse(1, "electronics", "Electronic devices");
        ProductResponse expectedResponse = new ProductResponse(productId, "Updated Product", "Updated Description", 25.99, categoryResponse);

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productMapper.productToProductResponse(existingProduct)).thenReturn(expectedResponse);

        ProductResponse result = productService.updateProduct(productId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        // given
        Integer productId = 999;
        ProductRequest request = new ProductRequest("Updated Product", "Updated Description", 25.99);

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.updateProduct(productId, request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        // given
        Integer productId = 1;
        Category category = new Category();
        category.setName("electronics");
        category.setDescription("Electronic devices");

        Product product = new Product(productId, "Test Product", "Description", 15.99, category, new HashSet<>());

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        // then
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        // given
        Integer productId = 999;

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).delete(any(Product.class));
    }
}