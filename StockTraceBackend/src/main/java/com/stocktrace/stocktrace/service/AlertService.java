package com.stocktrace.stocktrace.service;

import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final ProductRepository productRepository;

    public AlertService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getLowStockAlerts() {
        return productRepository.findLowStockProducts();
    }

    public long countLowStockProducts() {
        return productRepository.findLowStockProducts().size();
    }
}