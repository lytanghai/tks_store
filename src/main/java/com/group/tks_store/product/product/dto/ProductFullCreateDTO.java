package com.group.tks_store.product.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductFullCreateDTO {
    private ProductCreateDTO product;
    private VariantCreateDTO variant;
    @JsonProperty("variant_attributes")
    private List<VariantAttributeDTO> variantAttributes;

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

    public List<VariantAttributeDTO> getVariantAttribute() {
        return variantAttributes;
    }

    public void setVariantAttribute(List<VariantAttributeDTO> variantAttribute) {
        this.variantAttributes = variantAttribute;
    }
}
