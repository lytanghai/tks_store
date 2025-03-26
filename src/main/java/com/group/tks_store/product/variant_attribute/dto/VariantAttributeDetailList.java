package com.group.tks_store.product.variant_attribute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariantAttributeDetailList {
    private Integer variantAttributeId;
    private Integer variantId;
    private Integer attributeId;
    private String attributeName;
    private String value;
    private Integer groupNum;

    public VariantAttributeDetailList() {
    }
}
