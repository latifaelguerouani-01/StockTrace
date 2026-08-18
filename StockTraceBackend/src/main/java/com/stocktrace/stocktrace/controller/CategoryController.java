package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.dto.CategoryDto;
import com.stocktrace.stocktrace.entity.Category;
import com.stocktrace.stocktrace.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> all() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(categoryService.getCategoryById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(categoryService.createCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.ok(toDto(categoryService.updateCategory(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDto toDto(Category c) {
        CategoryDto dto = new CategoryDto(c.getId(), c.getName(), c.getDescription());
        dto.setProductsCount(categoryService.countProducts(c.getId()));
        return dto;
    }
}
