package com.group.tks_store.product.variant_attribute.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.enumz.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
public class VariantAttributeDTO {
    private Integer id;
    private Integer variantId;
    private Integer attributeId;
    private String value;
    private Integer groupNum;
    private String status = Status.ACTIVE.getValue();

    public VariantAttributeDTO() {}
}