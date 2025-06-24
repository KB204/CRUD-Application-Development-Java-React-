package net.demo.backendservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.dtos.product.ProductRequest;
import net.demo.backendservice.dtos.product.ProductResponse;
import net.demo.backendservice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProductService productService;

    @Test
    void shouldGetProductById() throws Exception {
        // given
        Integer productId = 1;
        CategoryResponse categoryResponse = new CategoryResponse(5,"test","test");
        ProductResponse productResponse = new ProductResponse(productId, "test product", "description", 10.0, categoryResponse);

        // when
        when(productService.findProductById(productId)).thenReturn(productResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(productResponse)));
    }

    @Test
    void shouldAddNewProduct() throws Exception {
        // given
        Integer categoryId = 1;
        ProductRequest request = new ProductRequest("product 1","test",6500.0);
        CategoryResponse categoryResponse = new CategoryResponse(5,"test","test");
        ProductResponse response = new ProductResponse(1,"product 1","test",6500.0,categoryResponse);

        // when
        when(productService.saveNewProduct(categoryId, request)).thenReturn(response);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/products?categoryId=" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void shouldEditExistingProduct() throws Exception {
        // given
        Integer productId = 1;
        CategoryResponse categoryResponse = new CategoryResponse(5,"test","test");
        ProductRequest request = new ProductRequest("product 1","test",6500.0);
        ProductResponse response = new ProductResponse(1,"product 1","test",6500.0,categoryResponse);

        // when
        when(productService.updateProduct(productId, request)).thenReturn(response);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/api/products/{id}",productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void shouldRemoveExistingProduct() throws Exception {
        // given
        Integer productId = 1;

        // when
        doNothing().when(productService).deleteProduct(productId);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/products/{id}",productId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}