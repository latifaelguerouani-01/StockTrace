package com.stocktrace.stocktrace.dto;

import com.stocktrace.stocktrace.entity.MovementType;

import java.time.LocalDateTime;

public class StockMovementDto {
    private Long id;
    private ProductDto product;
    private Integer quantity;
    private MovementType type;
    private String reason;
    private LocalDateTime createdAt;
    private Long userId;

    public StockMovementDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductDto getProduct() { return product; }
    public void setProduct(ProductDto product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public MovementType getType() { return type; }
    public void setType(MovementType type) { this.type = type; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
