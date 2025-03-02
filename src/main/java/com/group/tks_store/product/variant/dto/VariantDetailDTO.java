package com.group.tks_store.product.variant.dto;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;

import java.util.List;

public class VariantDetailDTO {
    private Long id;
    private String sku;
    private Double basePrice;
    private String currency;
    private Integer stockQuantity;
    private List<String> images;
    private List<VariantAttributeDTOV2> attributes;

    public VariantDetailDTO(Long id, String sku, Double basePrice, String currency, Integer stockQuantity,
                            List<String> images, List<VariantAttributeDTOV2> attributes) {
        this.id = id;
        this.sku = sku;
        this.basePrice = basePrice;
        this.currency = currency;
        this.stockQuantity = stockQuantity;
        this.images = images;
        this.attributes = attributes;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<VariantAttributeDTOV2> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<VariantAttributeDTOV2> attributes) {
        this.attributes = attributes;
    }
}
