package com.stocktrace.stocktrace.service;

import com.stocktrace.stocktrace.entity.Category;
import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.exception.BadRequestException;
import com.stocktrace.stocktrace.exception.ResourceNotFoundException;
import com.stocktrace.stocktrace.repository.CategoryRepository;
import com.stocktrace.stocktrace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    public Product createProduct(Product product, Long categoryId) {
        normalizeFrontendFields(product);
        if (product.getBarcode() != null && !product.getBarcode().isBlank()
                && productRepository.existsByBarcode(product.getBarcode())) {
            throw new BadRequestException("Barcode '" + product.getBarcode() + "' already exists");
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            product.setCategory(category);
            product.setType(category.getName());
        }

        if (product.getQuantity() == null) product.setQuantity(0);
        if (product.getMinStockAlert() == null) product.setMinStockAlert(5);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product details, Long categoryId) {
        normalizeFrontendFields(details);
        Product product = getProductById(id);

        String barcode = details.getBarcode();
        if (barcode != null && !barcode.isBlank()
                && !barcode.equals(product.getBarcode())
                && productRepository.existsByBarcode(barcode)) {
            throw new BadRequestException("Barcode '" + barcode + "' already exists");
        }

        product.setReference(details.getReference());
        product.setBarcode(barcode);
        product.setQuantity(details.getQuantity() == null ? 0 : details.getQuantity());
        product.setAffectation(details.getAffectation());
        product.setType(details.getType());
        product.setMark(details.getMark());
        product.setRemarks(details.getRemarks());
        product.setTag(details.getTag());
        product.setMinStockAlert(details.getMinStockAlert() == null ? 5 : details.getMinStockAlert());

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            product.setCategory(category);
            product.setType(category.getName());
        } else {
            product.setCategory(null);
        }

        return productRepository.save(product);
    }

    private void normalizeFrontendFields(Product product) {
        if ((product.getReference() == null || product.getReference().isBlank())
                && product.getName() != null && !product.getName().isBlank()) {
            product.setReference(product.getName());
        }
        if ((product.getBarcode() == null || product.getBarcode().isBlank())
                && product.getSku() != null && !product.getSku().isBlank()) {
            product.setBarcode(product.getSku());
        }
        if ((product.getRemarks() == null || product.getRemarks().isBlank())
                && product.getDescription() != null && !product.getDescription().isBlank()) {
            product.setRemarks(product.getDescription());
        }
    }

    public void deleteProduct(Long id) {
        productRepository.delete(getProductById(id));
    }
}
