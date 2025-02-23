//package com.group.tks_store.product.variant.entity;
//
//import com.group.tks_store.product.product.entity.ProductEntity;
//import org.springframework.data.annotation.Id;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.util.Date;
//
//public class VariantEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private ProductEntity product;
//
//    @Column(unique = true, nullable = false)
//    private String sku;
//
//    private BigDecimal basePrice;
//    private String currency;
//    private int stockQuantity;
//
//    @Column(name = "created_at", updatable = false)
//    private Date createdAt = new Date();
//
//    @Column(name = "last_updated_at")
//    private Date lastUpdatedAt = new Date();
//
//    public Integer getId() {
//        return id;
//    }
//
//    public Integer setId(Integer id) {
//        return this.id = id;
//    }
//
//    public ProductEntity getProduct() {
//        return product;
//    }
//
//    public void setProduct(ProductEntity product) {
//        this.product = product;
//    }
//
//    public String getSku() {
//        return sku;
//    }
//
//    public void setSku(String sku) {
//        this.sku = sku;
//    }
//
//    public BigDecimal getBasePrice() {
//        return basePrice;
//    }
//
//    public void setBasePrice(BigDecimal basePrice) {
//        this.basePrice = basePrice;
//    }
//
//    public String getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
//
//    public int getStockQuantity() {
//        return stockQuantity;
//    }
//
//    public void setStockQuantity(int stockQuantity) {
//        this.stockQuantity = stockQuantity;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Date getLastUpdatedAt() {
//        return lastUpdatedAt;
//    }
//
//    public void setLastUpdatedAt(Date lastUpdatedAt) {
//        this.lastUpdatedAt = lastUpdatedAt;
//    }
//}
