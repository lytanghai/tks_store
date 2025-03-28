package com.group.tks_store.common.util;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {

    public static List<VariantAttributeDTOV2> mapVariantAttributes(String attributes) {
        List<VariantAttributeDTOV2> result = new ArrayList<>();

        String[] parts = attributes.split("&");
        if (parts.length < 2) return result;

        String attributePart = parts[0];
        String[] groupNums = parts[1].split(",");

        String[] attributesArray = attributePart.split(",");
        for (int i = 0; i < attributesArray.length; i++) {
            String[] attrParts = attributesArray[i].split(":");

            if (attrParts.length >= 3) {
                VariantAttributeDTOV2 dto = new VariantAttributeDTOV2();

                dto.setId(Integer.parseInt(attrParts[0].trim())); // ID
                dto.setName(attrParts[1].trim()); // Name
                dto.setValue(attrParts[2].trim()); // Value

                int groupNum = (i < groupNums.length) ? Integer.parseInt(groupNums[i].trim()) : 0;
                dto.setGroupNum(groupNum);

                result.add(dto);
            }
        }

        return result;
    }

}
