package com.stocktrace.stocktrace.dto;

public class DashboardStatsResponse {

    private long totalProducts;
    private long totalCategories;
    private long lowStockCount;
    private double totalStockValue;

    public DashboardStatsResponse() {
    }

    public DashboardStatsResponse(long totalProducts, long totalCategories, long lowStockCount, double totalStockValue) {
        this.totalProducts = totalProducts;
        this.totalCategories = totalCategories;
        this.lowStockCount = lowStockCount;
        this.totalStockValue = totalStockValue;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public double getTotalStockValue() {
        return totalStockValue;
    }

    public void setTotalStockValue(double totalStockValue) {
        this.totalStockValue = totalStockValue;
    }
}