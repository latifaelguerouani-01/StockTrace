package com.stocktrace.stocktrace.service;

import com.stocktrace.stocktrace.entity.Category;
import com.stocktrace.stocktrace.exception.BadRequestException;
import com.stocktrace.stocktrace.exception.ResourceNotFoundException;
import com.stocktrace.stocktrace.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final com.stocktrace.stocktrace.repository.ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                            com.stocktrace.stocktrace.repository.ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new BadRequestException("Category name is required");
        }
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new BadRequestException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category details) {
        Category category = getCategoryById(id);
        if (details.getName() == null || details.getName().isBlank()) {
            throw new BadRequestException("Category name is required");
        }
        if (!category.getName().equalsIgnoreCase(details.getName())
                && categoryRepository.existsByNameIgnoreCase(details.getName())) {
            throw new BadRequestException("Category already exists");
        }
        category.setName(details.getName());
        category.setDescription(details.getDescription());
        return categoryRepository.save(category);
    }

    public long countProducts(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (countProducts(id) > 0) {
            throw new BadRequestException("Cannot delete a category containing products");
        }
        categoryRepository.delete(category);
    }
}
