package com.group.tks_store.product.variant_image.dto;

import com.group.tks_store.product.variant.dto.VariantCreateDTO;

import java.util.Date;

public class VariantImageProductListDTO {

    private Integer id;
    private VariantCreateDTO variant;
    private String image;
    private Date createdAt;
    private Date lastUpdatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VariantCreateDTO getVariant() {
        return variant;
    }

    public void setVariant(VariantCreateDTO variant) {
        this.variant = variant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
