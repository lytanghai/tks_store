package com.group.tks_store.product.variant.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.product.dto.ProductCreateDTO;

import java.math.BigDecimal;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VariantListDTO {
    private Integer id;
    private String sku;
    private ProductCreateDTO product;
    private BigDecimal basePrice;
    private String currency;
    private Integer stockQuantity;
    private String status;
    private Date createdAt;
    private Date lastUpdatedAt;
    private String productNameEn;
    private String productNameKh;

    public VariantListDTO(Integer id, String sku, BigDecimal basePrice, String currency, Integer stockQuantity, String status, Date createdAt, Date lastUpdatedAt, String productNameEn, String productNameKh) {
        this.id = id;
        this.sku = sku;
        this.basePrice = basePrice;
        this.currency = currency;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.productNameEn = productNameEn;
        this.productNameKh = productNameKh;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductCreateDTO getProduct() {
        return product;
    }

    public void setProduct(ProductCreateDTO product) {
        this.product = product;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    public String getProductNameKh() {
        return productNameKh;
    }

    public void setProductNameKh(String productNameKh) {
        this.productNameKh = productNameKh;
    }
}
