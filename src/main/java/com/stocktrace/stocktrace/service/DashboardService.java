package com.stocktrace.stocktrace.service;

import com.stocktrace.stocktrace.repository.CategoryRepository;
import com.stocktrace.stocktrace.repository.ProductRepository;
import com.stocktrace.stocktrace.repository.StockMovementRepository;
import com.stocktrace.stocktrace.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StockMovementRepository stockMovementRepository;

    public DashboardService(ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository,
                            StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalProducts", productRepository.count());
        stats.put("totalCategories", categoryRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalMovements", stockMovementRepository.count());
        stats.put("lowStockCount", productRepository.findLowStockProducts().size());

        return stats;
    }
}