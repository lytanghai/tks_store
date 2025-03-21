package com.group.tks_store.common.util;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {

    public static List<VariantAttributeDTOV2> mapVariantAttributes(String attributes) {
        List<VariantAttributeDTOV2> variantAttributes = new ArrayList<>();
        if (attributes != null) {
            String[] attributeEntries = attributes.split(",");
            for (String attr : attributeEntries) {
                String[] parts = attr.split(":");
                if (parts.length == 3) {
                    variantAttributes.add(new VariantAttributeDTOV2(Integer.valueOf(parts[0]), String.valueOf(parts[1]), parts[2]));
                }
            }
        }
        return variantAttributes;
    }
}
