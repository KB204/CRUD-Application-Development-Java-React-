package net.demo.backendservice.utils;

import net.demo.backendservice.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class ProductSpecification {

    public static Specification<Product> filterWithoutConditions() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(name)
                        .map(product -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Product> priceEqual(Double price) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(price)
                        .map(product -> criteriaBuilder.equal(root.get("price"),price))
                        .orElse(criteriaBuilder.conjunction());
    }

    public static Specification<Product> categoryEqual(String category) {
        return (root, query, criteriaBuilder) ->
                Optional.ofNullable(category)
                        .map(product -> criteriaBuilder.equal(root.get("category").get("name"), category))
                        .orElse(criteriaBuilder.conjunction());
    }
}
