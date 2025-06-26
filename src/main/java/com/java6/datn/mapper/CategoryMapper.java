package com.java6.datn.Mapper;

import com.java6.datn.DTO.CategoryDTO;
import com.java6.datn.Entity.Category;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryID(category.getCategoryID());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public static Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryID(dto.getCategoryID());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }
}

