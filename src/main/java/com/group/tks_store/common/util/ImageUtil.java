package com.group.tks_store.common.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageUtil {

    public static String convertImageToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = Base64.encodeBase64(file.getBytes());
        return new String(fileContent);
    }
}
