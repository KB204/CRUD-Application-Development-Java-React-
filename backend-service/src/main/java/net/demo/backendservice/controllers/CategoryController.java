package net.demo.backendservice.controllers;

import jakarta.validation.Valid;
import net.demo.backendservice.dtos.category.CategoryRequest;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.dtos.category.CategoryResponseDto;
import net.demo.backendservice.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryResponse> getAllCategories(@RequestParam(required = false) String name, Pageable pageable) {
        return categoryService.findAllCategories(name, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Integer id) {
        CategoryResponse response = categoryService.findCategoryById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<CategoryResponseDto> getCategoryDetails(@PathVariable Integer id) {
        CategoryResponseDto response = categoryService.categoryDetails(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.saveNewCategory(categoryRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer id, @RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
