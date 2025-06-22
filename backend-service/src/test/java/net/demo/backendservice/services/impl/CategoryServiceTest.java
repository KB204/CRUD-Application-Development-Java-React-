package net.demo.backendservice.services.impl;

import net.demo.backendservice.dao.CategoryRepository;
import net.demo.backendservice.dtos.category.CategoryRequest;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.entities.Category;
import net.demo.backendservice.exceptions.ResourceAlreadyExists;
import net.demo.backendservice.exceptions.ResourceNotFoundException;
import net.demo.backendservice.mappers.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldFindCategoryById() {
        // given
        Category category = new Category();
        category.setName("test");
        category.setDescription("this is a test");
        CategoryResponse categoryResponse = new CategoryResponse(1,"test","this is a test");

        // when
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToCategoryResponse(category)).thenReturn(categoryResponse);
        CategoryResponse response = categoryService.findCategoryById(category.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(categoryResponse).isEqualTo(response);
    }

    @Test
    void shouldNotFindCategoryById() {
        // given
        Integer categoryId = 10;

        // when
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> categoryService.findCategoryById(categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldFindAllCategoriesWithPaginationAndFilter() {
        // given
        String searchName = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());

        List<Category> categories = List.of(
                new Category(1, "test1", "description1", new ArrayList<>()),
                new Category(2, "test2", "description2", new ArrayList<>())
        );

        List<CategoryResponse> categoryResponses = List.of(
                new CategoryResponse(1, "test1", "description1"),
                new CategoryResponse(2, "test2", "description2")
        );

        Page<Category> categoryPage = new PageImpl<>(categories);

        // when
        when(categoryRepository.findAll(any(Specification.class), eq(expectedPageable))).thenReturn(categoryPage);
        when(categoryMapper.categoryToCategoryResponse(categories.get(0))).thenReturn(categoryResponses.get(0));
        when(categoryMapper.categoryToCategoryResponse(categories.get(1))).thenReturn(categoryResponses.get(1));

        Page<CategoryResponse> result = categoryService.findAllCategories(searchName, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0)).isEqualTo(categoryResponses.get(0));
        assertThat(result.getContent().get(1)).isEqualTo(categoryResponses.get(1));
    }

    @Test
    void shouldSaveNewCategory() {
        // given
        CategoryRequest request = new CategoryRequest("New Category", "New Description");
        Category category = new Category();
        category.setId(1);
        category.setName("New Category");
        category.setDescription("New Description");

        Category savedCategory = new Category();
        savedCategory.setId(1);
        savedCategory.setName("New Category");
        savedCategory.setDescription("New Description");

        CategoryResponse expectedResponse = new CategoryResponse(1, "New Category", "New Description");

        // when
        when(categoryRepository.findByNameIgnoreCase(request.name())).thenReturn(Optional.empty());
        when(categoryMapper.requestDtoToCategory(request)).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponse(savedCategory)).thenReturn(expectedResponse);

        CategoryResponse result = categoryService.saveNewCategory(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldNotSaveNewCategoryWhenNameAlreadyExists() {
        // given
        CategoryRequest request = new CategoryRequest("Existing Category", "Description");
        Category existingCategory = new Category();
        existingCategory.setName("Existing Category");
        existingCategory.setDescription("Description");

        // when
        when(categoryRepository.findByNameIgnoreCase(request.name())).thenReturn(Optional.of(existingCategory));

        // then
        assertThatThrownBy(() -> categoryService.saveNewCategory(request))
                .isInstanceOf(ResourceAlreadyExists.class);

        verify(categoryRepository, never()).save(existingCategory);
    }

    @Test
    void shouldUpdateExistingCategory() {
        // given
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest("Updated Name", "Updated Description");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Name");
        updatedCategory.setDescription("Updated Description");

        CategoryResponse expectedResponse = new CategoryResponse(categoryId, "Updated Name", "Updated Description");

        // when
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.categoryToCategoryResponse(updatedCategory)).thenReturn(expectedResponse);

        CategoryResponse result = categoryService.updateCategory(categoryId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        verify(categoryRepository).save(updatedCategory);
    }

    @Test
    void shouldNotUpdateNonExistentCategory() {
        // given
        Category category = new Category();
        category.setId(1);
        category.setName("New Name");
        category.setDescription("New Description");
        CategoryRequest request = new CategoryRequest("Updated Name", "Updated Description");

        // when
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> categoryService.updateCategory(category.getId(), request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).save(category);
    }

    @Test
    void shouldDeleteCategory() {
        // given
        Integer categoryId = 1;
        Category category = new Category(categoryId, "Test Category", "Test Description", new ArrayList<>());

        // when
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        // then
        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldNotDeleteNonExistentCategory() {
        // given
        Category category = new Category();
        category.setId(999);
        category.setName("Test Category");
        category.setDescription("Test Description");

        // when
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> categoryService.deleteCategory(category.getId()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).delete(category);
    }
}