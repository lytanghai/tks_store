package com.group.tks_store.product.variant_image.dto;

import org.springframework.web.multipart.MultipartFile;

public class VariantImageCreateDTO {

    private Integer id;
    private Integer variantId;
    private MultipartFile image;
    private String imageType = "N/A";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
