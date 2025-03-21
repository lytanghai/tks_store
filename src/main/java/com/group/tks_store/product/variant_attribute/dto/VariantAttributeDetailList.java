package com.group.tks_store.product.variant_attribute.dto;

public class VariantAttributeDetailList {
    private Integer variantAttributeId;
    private Integer variantId;
    private Integer attributeId;
    private String attributeName;
    private String value;

    public VariantAttributeDetailList() {
    }

    public VariantAttributeDetailList(Integer variantAttributeId, Integer variantId, Integer attributeId, String attributeName, String value) {
        this.variantAttributeId = variantAttributeId;
        this.variantId = variantId;
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.value = value;
    }

    public Integer getVariantAttributeId() {
        return variantAttributeId;
    }

    public void setVariantAttributeId(Integer id) {
        this.variantAttributeId = id;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeNameEn) {
        this.attributeName = attributeNameEn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
