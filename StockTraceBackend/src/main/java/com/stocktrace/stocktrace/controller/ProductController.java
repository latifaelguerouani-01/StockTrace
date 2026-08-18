package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.dto.ProductDto;
import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts().stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(productService.getProductById(id)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId).stream().map(this::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody Product product) {
        Long categoryId = product.getCategory() == null ? null : product.getCategory().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDto(productService.createProduct(product, categoryId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody Product product) {
        Long categoryId = product.getCategory() == null ? null : product.getCategory().getId();
        return ResponseEntity.ok(toDto(productService.updateProduct(id, product, categoryId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getReference());
        dto.setSku(p.getBarcode());
        dto.setBarcode(p.getBarcode());
        dto.setReference(p.getReference());
        dto.setQuantity(p.getQuantity());
        dto.setAffectation(p.getAffectation());
        dto.setType(p.getType());
        dto.setMark(p.getMark());
        dto.setRemarks(p.getRemarks());
        dto.setTag(p.getTag());
        dto.setMinStockAlert(p.getMinStockAlert());
        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
            dto.setCategoryName(p.getCategory().getName());
        }
        return dto;
    }
}
