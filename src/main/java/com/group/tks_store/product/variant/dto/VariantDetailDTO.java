package com.group.tks_store.product.variant.dto;

import com.group.tks_store.product.images.dto.ImageDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;

import java.util.List;

public class VariantDetailDTO {
    private Integer id;
    private String sku;
    private Double basePrice;
    private String basePriceCurrency;
    private Integer stockQuantity;
    private List<ImageDTO> images;
    private List<VariantAttributeDTOV2> attributes;

    public VariantDetailDTO(Integer id, String sku, Double basePrice, String basePriceCurrency, Integer stockQuantity,
                            List<ImageDTO> images, List<VariantAttributeDTOV2> attributes) {
        this.id = id;
        this.sku = sku;
        this.basePrice = basePrice;
        this.basePriceCurrency = basePriceCurrency;
        this.stockQuantity = stockQuantity;
        this.images = images;
        this.attributes = attributes;
    }

    // Getters & Setters

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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getBasePriceCurrency() {
        return basePriceCurrency;
    }

    public void setBasePriceCurrency(String currency) {
        this.basePriceCurrency = currency;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public List<VariantAttributeDTOV2> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<VariantAttributeDTOV2> attributes) {
        this.attributes = attributes;
    }
}
