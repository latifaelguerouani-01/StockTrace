package com.stocktrace.stocktrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "barcode")
    private String barcode;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "affectation")
    private String affectation;

    @Column(name = "type")
    private String type;

    @Column(name = "mark")
    private String mark;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "tag")
    private String tag;

    @Column(name = "min_stock_alert", nullable = false)
    private Integer minStockAlert = 5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" , nullable = true)
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<StockMovement> stockMovements = new ArrayList<>();

    // JSON compatibility with the existing React frontend.
    @Transient private String name;
    @Transient private String sku;
    @Transient private String description;

    public Product() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

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

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<StockMovement> getStockMovements() { return stockMovements; }
    public void setStockMovements(List<StockMovement> stockMovements) { this.stockMovements = stockMovements; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
