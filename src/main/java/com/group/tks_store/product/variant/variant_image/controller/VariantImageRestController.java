package com.group.tks_store.product.variant.variant_image.controller;


import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.variant.dto.VariantUpdateDto;
import com.group.tks_store.product.variant.variant_image.dto.VariantImageCreateDTO;
import com.group.tks_store.product.variant.variant_image.dto.VariantImageListDTO;
import com.group.tks_store.product.variant.variant_image.dto.VariantImageUpdateDto;
import com.group.tks_store.product.variant.variant_image.entity.VariantImageEntity;
import com.group.tks_store.product.variant.variant_image.service.VariantImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void create(@RequestBody VariantImageCreateDTO variantImageCreateDTO) throws ParseException {
        variantImageService.create(variantImageCreateDTO);
    }

    @PostMapping("/update")
    public String delete(@RequestBody VariantImageUpdateDto variantImageUpdateDto) throws ParseException {
        variantImageService.update(variantImageUpdateDto);
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
