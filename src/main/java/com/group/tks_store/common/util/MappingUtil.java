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
        String groupNumPart = parts[1];

        String[] attributesArray = attributePart.split(",");
        String[] groupNums = groupNumPart.split(",");

        // Ensure both arrays have the same length
        int length = Math.min(attributesArray.length, groupNums.length);

        for (int i = 0; i < length; i++) {
            String[] attrParts = attributesArray[i].split(":");

            if (attrParts.length >= 3) {
                VariantAttributeDTOV2 dto = new VariantAttributeDTOV2();

                dto.setId(Integer.parseInt(attrParts[0].trim())); // ID
                dto.setName(attrParts[1].trim());                // Name
                dto.setValue(attrParts[2].trim());               // Value

                // Directly use the corresponding group number
                int groupNum = Integer.parseInt(groupNums[i].trim());
                dto.setGroupNum(groupNum);

                result.add(dto);
            }
        }

        return result;
    }



}
