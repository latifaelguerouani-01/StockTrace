package com.stocktrace.stocktrace.dto;

public class ProductDto {
    private Long id;
    private String name;
    private String sku;
    private String barcode;
    private String reference;
    private Integer quantity;
    private String affectation;
    private String type;
    private String mark;
    private String remarks;
    private String tag;
    private Integer minStockAlert;
    private Long categoryId;
    private String categoryName;

    public ProductDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Frontend compatibility: name = reference.
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Frontend compatibility: sku = barcode.
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getAffectation() { return affectation; }
    public void setAffectation(String affectation) { this.affectation = affectation; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMark() { return mark; }
    public void setMark(String mark) { this.mark = mark; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Integer getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(Integer minStockAlert) { this.minStockAlert = minStockAlert; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
