package com.stocktrace.stocktrace.dto;

public class AlertDto {

    private Long productId;
    private String productName;
    private Integer currentQuantity;
    private Integer minStockAlert;

    public AlertDto() {
    }

    public AlertDto(Long productId, String productName, Integer currentQuantity, Integer minStockAlert) {
        this.productId = productId;
        this.productName = productName;
        this.currentQuantity = currentQuantity;
        this.minStockAlert = minStockAlert;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Integer getMinStockAlert() {
        return minStockAlert;
    }

    public void setMinStockAlert(Integer minStockAlert) {
        this.minStockAlert = minStockAlert;
    }
}