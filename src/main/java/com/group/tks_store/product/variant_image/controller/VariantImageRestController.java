package com.group.tks_store.product.variant_image.controller;


import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.variant_image.dto.VariantImageListDTO;
import com.group.tks_store.product.variant_image.entity.VariantImageEntity;
import com.group.tks_store.product.variant_image.service.VariantImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/variant-image")
public class VariantImageRestController {

    @Autowired
    private VariantImageService variantImageService;


    @PostMapping("/create")
    public void createVariantImage(@RequestParam("variant_id") Integer variantId,
                                     @RequestParam("image") MultipartFile image) throws ParseException, IOException {
        variantImageService.create(variantId, image);
    }

    @PostMapping("/update")
    public String delete(@RequestParam("variant_image_id") Integer variantImageId,
                         @RequestParam("variant_id") Integer variantId,
                         @RequestParam("image") MultipartFile image) throws ParseException, IOException {
        variantImageService.update(variantImageId, variantId, image);
        return "redirect:/variant";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) throws ParseException {
        variantImageService.delete(id);
        return "redirect:/variant";
    }

    // 🔹 Get all products
    @GetMapping("/list")
    public List<VariantImageEntity> getAllVariantImages() {
        return variantImageService.getAllVariantImages();
    }

    // 🔹 Get paginated active products
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>>  getActiveVariants(Pageable pageable) {
        Page<VariantImageListDTO> variantPage = variantImageService.getActiveVariantImage(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("variants-image", variantPage.getContent());
        response.put("currentPage", variantPage.getNumber());
        response.put("totalItems", variantPage.getTotalElements());
        response.put("totalPages", variantPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

}
