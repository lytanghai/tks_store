package com.group.tks_store.product.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant_image.dto.VariantImageCreateDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDTO {
    private ProductCreateDTO product;
    private VariantCreateDTO variant;
    @JsonProperty("variant_attributes")
    private List<VariantAttributeDTO> variantAttributes;
    @JsonProperty("images")
    private List<VariantImageCreateDTO> images;

    public ProductCreateDTO getProduct() {
        return product;
    }

    public void setProduct(ProductCreateDTO product) {
        this.product = product;
    }

    public VariantCreateDTO getVariant() {
        return variant;
    }

    public void setVariant(VariantCreateDTO variant) {
        this.variant = variant;
    }

    public List<VariantAttributeDTO> getVariantAttributes() {
        return variantAttributes;
    }

    public void setVariantAttributes(List<VariantAttributeDTO> variantAttributes) {
        this.variantAttributes = variantAttributes;
    }

    public List<VariantImageCreateDTO> getImages() {
        return images;
    }

    public void setImages(List<VariantImageCreateDTO> images) {
        this.images = images;
    }

    public List<VariantImageCreateDTO> getVariantImage() {
        return images;
    }

    public void setVariantImage(List<VariantImageCreateDTO> variantImage) {
        this.images = variantImage;
    }
}
