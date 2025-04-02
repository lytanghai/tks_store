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

        // Filter out group numbers that are at every third index (0, 3, 6, 9, ...)
        List<String> selectedGroupNums = new ArrayList<>();
        for (int i = 0; i < groupNums.length; i++) {
            // Only select every third index
            if (i % 3 == 0) {
                selectedGroupNums.add(groupNums[i]);
            }
        }

        for (int i = 0; i < attributesArray.length; i++) {
            String[] attrParts = attributesArray[i].split(":");

            if (attrParts.length >= 3) {
                VariantAttributeDTOV2 dto = new VariantAttributeDTOV2();

                dto.setId(Integer.parseInt(attrParts[0].trim())); // ID
                dto.setName(attrParts[1].trim());                // Name
                dto.setValue(attrParts[2].trim());               // Value

                int groupNum = (i < selectedGroupNums.size()) ? Integer.parseInt(selectedGroupNums.get(i).trim()) : 0;
                dto.setGroupNum(groupNum);

                result.add(dto);
            }
        }

        return result;
    }


}
