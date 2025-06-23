package net.demo.backendservice.mappers;

import net.demo.backendservice.dtos.category.CategoryRequest;
import net.demo.backendservice.dtos.category.CategoryResponse;
import net.demo.backendservice.dtos.category.CategoryResponseDto;
import net.demo.backendservice.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse categoryToCategoryResponse(Category category);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category requestDtoToCategory(CategoryRequest request);
    CategoryResponseDto categoryToCategoryResponseDto(Category category);
}
