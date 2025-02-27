package com.group.tks_store.product.variant_attribute.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.enumz.Status;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VariantAttributeDTO {
    private Integer id;
    private Integer variantId;
    private Integer attributeId;
    private String value;
    private String status = Status.ACTIVE.getValue();

    public VariantAttributeDTO() {}

    public VariantAttributeDTO(Integer id, Integer variantId, Integer attributeId, String value, String status) {
        this.id = id;
        this.variantId = variantId;
        this.attributeId = attributeId;
        this.value = value;
        this.status = status;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}