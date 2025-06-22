package net.demo.backendservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.demo.backendservice.dtos.category.CategoryRequest;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldGetCategoryById() throws Exception {
        // given
        Integer categoryId = 1;
        CategoryResponse category = new CategoryResponse(categoryId, "test", "description");

        // When
        when(categoryService.findCategoryById(categoryId)).thenReturn(category);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/categories/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(category)));
    }

    @Test
    void shouldCreateNewCategory() throws Exception {
        // given
        CategoryRequest request = new CategoryRequest("New Category", "New Description");
        CategoryResponse response = new CategoryResponse(1, "New Category", "New Description");

        // When
        when(categoryService.saveNewCategory(request)).thenReturn(response);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void shouldUpdateExistingCategory() throws Exception {
        // given
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest("Updated Category", "Updated Description");
        CategoryResponse response = new CategoryResponse(categoryId, "Updated Category", "Updated Description");

        // When
        when(categoryService.updateCategory(categoryId,request)).thenReturn(response);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void shouldDeleteExistingCategory() throws Exception {
        // Given
        Integer categoryId = 1;

        // When
        doNothing().when(categoryService).deleteCategory(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/categories/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}