package com.java6.datn.service;

import com.java6.datn.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Integer id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO);
    void deleteCategory(Integer id);
}

