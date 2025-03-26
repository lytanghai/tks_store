package com.group.tks_store.common.util;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {

    public static List<VariantAttributeDTOV2> mapVariantAttributes(String attributes) {
        List<VariantAttributeDTOV2> result = new ArrayList<>();

        // Split attributes and group numbers
        String[] parts = attributes.split("&");
        if (parts.length < 2) return result; // Ensure valid data

        String attributePart = parts[0]; // Attributes section
        String[] groupNums = parts[1].split(","); // Extract group numbers

        // Process each attribute
        String[] attributesArray = attributePart.split(",");
        for (int i = 0; i < attributesArray.length; i++) {
            String[] attrParts = attributesArray[i].split(":");

            if (attrParts.length >= 3 && i < groupNums.length) { // Ensure correct format and valid group index
                VariantAttributeDTOV2 dto = new VariantAttributeDTOV2();
                dto.setId(Integer.parseInt(attrParts[0])); // ID
                dto.setName(attrParts[1]); // Name
                dto.setValue(attrParts[2]); // Value
                dto.setGroupNum(Integer.parseInt(groupNums[i])); // Assign correct group_num

                result.add(dto);
            }
        }

        return result;
    }
}
