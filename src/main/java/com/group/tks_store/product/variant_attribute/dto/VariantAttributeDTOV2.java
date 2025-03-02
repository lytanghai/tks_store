package com.group.tks_store.product.variant_attribute.dto;

public class VariantAttributeDTOV2 {
    private Integer attributeId;
    private String value;

    public VariantAttributeDTOV2(Integer attributeId, String value) {
        this.attributeId = attributeId;
        this.value = value;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}