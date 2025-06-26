package com.java6.datn.controller;

import com.java6.datn.dto.CategoryDTO;
import com.java6.datn.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
//@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }
}

