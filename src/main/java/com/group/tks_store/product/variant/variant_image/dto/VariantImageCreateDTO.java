package com.group.tks_store.product.variant.variant_image.dto;

import com.group.tks_store.product.variant.dto.VariantCreateDTO;

public class VariantImageCreateDTO {

    private Integer id;
    private VariantCreateDTO variant;
    private String image;
    private String imageType;

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

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
