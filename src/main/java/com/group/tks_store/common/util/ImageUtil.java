package com.group.tks_store.common.util;

import com.group.tks_store.product.images.dto.ImageDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    public static String convertImageToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = Base64.encodeBase64(file.getBytes());
        return new String(fileContent);
    }

    public static List<String> mapImageUUID(String image) {
        List<String> images = new ArrayList<>();
        if (image != null) {
            String[] imageEntries = image.split(",");
            for (String index : imageEntries) {
                images.add(index);
            }
        }
        return images;
    }

    public static List<ImageDTO> mapImageUUIDPair(String images) {
        List<ImageDTO> imagePair = new ArrayList<>();
        if (images != null) {
            String[] imageEntries = images.split(",");
            for (String attr : imageEntries) {
                String[] parts = attr.split(":");
                if (parts.length == 2) {
                    imagePair.add(new ImageDTO(String.valueOf(parts[0]), parts[1]));
                }
            }
        }
        return imagePair;
    }
}
