package com.group.tks_store.product.variant_attribute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeDTOV2 {
    private Integer id;
    private String name;
    private String value;
    Integer groupNum;

}