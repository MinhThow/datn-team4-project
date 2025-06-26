package com.java6.datn.repository.specification;

import com.java6.datn.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    /**
     * Creates a specification for searching products by keyword in the name field.
     * @param keyword the search keyword
     * @return Specification for keyword search
     */
    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Always true condition
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), 
                "%" + keyword.toLowerCase().trim() + "%"
            );
        };
    }

    /**
     * Creates a specification for filtering products by category ID.
     * @param categoryId the category ID to filter by
     * @return Specification for category filtering
     */
    public static Specification<Product> hasCategory(Integer categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction(); // Always true condition
            }
            return criteriaBuilder.equal(root.get("categoryID"), categoryId);
        };
    }

    /**
     * Combines multiple specifications for comprehensive product filtering.
     * @param keyword search keyword for product name
     * @param categoryId category ID to filter by
     * @return Combined specification
     */
    public static Specification<Product> getSpecs(String keyword, Integer categoryId) {
        return hasKeyword(keyword).and(hasCategory(categoryId));
    }
} 