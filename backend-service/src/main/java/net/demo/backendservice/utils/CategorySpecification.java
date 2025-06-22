package net.demo.backendservice.utils;

import net.demo.backendservice.entities.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class CategorySpecification {

    public static Specification<Category> filterWithoutConditions() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
    }

    public static Specification<Category> nameLike(String name) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(name)
                        .map(category -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }
}
