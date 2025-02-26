package com.group.tks_store.product.product.dto;

import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;

public class ProductFullCreateDTO {
    private ProductCreateDTO product;
    private VariantCreateDTO variant;
    private VariantAttributeDTO variantAttribute;

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

    public VariantAttributeDTO getVariantAttribute() {
        return variantAttribute;
    }

    public void setVariantAttribute(VariantAttributeDTO variantAttribute) {
        this.variantAttribute = variantAttribute;
    }
}
