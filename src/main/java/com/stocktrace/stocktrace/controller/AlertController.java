package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.dto.ProductDto;
import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto>> lowStock() {
        return ResponseEntity.ok(alertService.getLowStockAlerts().stream().map(this::toDto).toList());
    }

    @GetMapping("/low-stock/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(alertService.countLowStockProducts());
    }

    private ProductDto toDto(Product p) {
        ProductDto d = new ProductDto();
        d.setId(p.getId());
        d.setName(p.getReference());
        d.setSku(p.getBarcode());
        d.setBarcode(p.getBarcode());
        d.setReference(p.getReference());
        d.setQuantity(p.getQuantity());
        d.setAffectation(p.getAffectation());
        d.setType(p.getType());
        d.setMark(p.getMark());
        d.setRemarks(p.getRemarks());
        d.setTag(p.getTag());
        d.setMinStockAlert(p.getMinStockAlert());
        if (p.getCategory() != null) {
            d.setCategoryId(p.getCategory().getId());
            d.setCategoryName(p.getCategory().getName());
        }
        return d;
    }
}
