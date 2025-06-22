package net.demo.backendservice.services.impl;

import net.demo.backendservice.dao.CategoryRepository;
import net.demo.backendservice.dtos.category.CategoryRequest;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.entities.Category;
import net.demo.backendservice.exceptions.ResourceAlreadyExists;
import net.demo.backendservice.exceptions.ResourceNotFoundException;
import net.demo.backendservice.mappers.CategoryMapper;
import net.demo.backendservice.services.CategoryService;
import net.demo.backendservice.utils.CategorySpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    // method to fetch data with pagination, sorting, and dynamic filtering (data can be fetched with and without filtering)
    @Override
    public Page<CategoryResponse> findAllCategories(String name, Pageable pageable) {
        Specification<Category> specification = CategorySpecification.filterWithoutConditions()
                .and(CategorySpecification.nameLike(name));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name").ascending());

        List<CategoryResponse> categories =  categoryRepository.findAll(specification, pageable)
                .stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();

        return new PageImpl<>(categories);
    }

    @Override
    public CategoryResponse findCategoryById(Integer id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Category with id %s not found", id)));

        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    public CategoryResponse saveNewCategory(CategoryRequest request) {
        categoryRepository.findByNameIgnoreCase(request.name()).ifPresent(category -> {
            throw new ResourceAlreadyExists(format("Category with name %s already exists", request.name()));
        });

        var category = categoryMapper.requestDtoToCategory(request);
        categoryRepository.save(category);
        logger.info("Saved new category: {}", category);

        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Category with id %s not found", id)));

        category.setName(request.name());
        category.setDescription(request.description());

        categoryRepository.save(category);
        logger.info("Updated category: {}", category);

        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Category with id %s not found", id)));

        categoryRepository.delete(category);
        logger.info("Deleted category: {}", category);
    }
}
