package com.stocktrace.stocktrace.repository;

import com.stocktrace.stocktrace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcode);
    boolean existsByBarcode(String barcode);
    long countByCategoryId(Long categoryId);
    List<Product> findByCategoryId(Long categoryId);

    @Query("select p from Product p where p.quantity <= p.minStockAlert")
    List<Product> findLowStockProducts();
}
