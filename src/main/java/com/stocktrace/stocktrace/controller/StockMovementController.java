package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.dto.ProductDto;
import com.stocktrace.stocktrace.dto.StockMovementDto;
import com.stocktrace.stocktrace.entity.Product;
import com.stocktrace.stocktrace.entity.StockMovement;
import com.stocktrace.stocktrace.service.StockMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {
    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StockMovementDto>> all() {
        return ResponseEntity.ok(service.getAllMovements().stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(service.getMovementById(id)));
    }

    @PostMapping
    public ResponseEntity<StockMovementDto> create(
            @RequestBody StockMovement movement,
            @RequestParam Long productId,
            @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDto(service.createMovement(movement, productId, userId)));
    }

    private StockMovementDto toDto(StockMovement m) {
        StockMovementDto d = new StockMovementDto();
        d.setId(m.getId());
        d.setQuantity(m.getQuantity());
        d.setType(m.getType());
        d.setReason(m.getReason());
        d.setCreatedAt(m.getCreatedAt());
        if (m.getUser() != null) d.setUserId(m.getUser().getId());

        Product p = m.getProduct();
        if (p != null) {
            ProductDto pd = new ProductDto();
            pd.setId(p.getId());
            pd.setName(p.getReference());
            pd.setSku(p.getBarcode());
            pd.setBarcode(p.getBarcode());
            pd.setReference(p.getReference());
            pd.setQuantity(p.getQuantity());
            pd.setAffectation(p.getAffectation());
            pd.setType(p.getType());
            pd.setMark(p.getMark());
            pd.setRemarks(p.getRemarks());
            pd.setTag(p.getTag());
            pd.setMinStockAlert(p.getMinStockAlert());
            if (p.getCategory() != null) {
                pd.setCategoryId(p.getCategory().getId());
                pd.setCategoryName(p.getCategory().getName());
            }
            d.setProduct(pd);
        }
        return d;
    }
}
