package com.group.tks_store.product.variant.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.product.dto.ProductCreateDTO;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VariantCreateDTO {

    private Integer id;
    private String sku;
    private ProductCreateDTO product;
    private Double basePrice;
    private String currency;
    private Integer stockQuantity;

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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
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
}
