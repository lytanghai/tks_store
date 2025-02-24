package com.group.tks_store.product.variant.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant.dto.VariantListDTO;
import com.group.tks_store.product.variant.dto.VariantUpdateDto;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.service.VariantService;
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
@RequestMapping("/rest/variant")
public class VariantRestController {

    @Autowired
    private VariantService variantService;


    @PostMapping("/create")
    public void create(@RequestBody VariantCreateDTO variantCreateDTO) throws ParseException {
        variantService.create(variantCreateDTO);
    }

    @PostMapping("/update")
    public String delete(@RequestBody VariantUpdateDto variantUpdateDto) throws ParseException {
        variantService.update(variantUpdateDto);
        return "redirect:/variant";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) throws ParseException {
        variantService.delete(id);
        return "redirect:/variant";
    }

    // 🔹 Get all products
    @GetMapping("/list")
    public List<VariantEntity> getAllVariants() {
        return variantService.getAllVariants();
    }

    // 🔹 Get paginated active products
    @GetMapping("/variants/active")
    public ResponseEntity<Map<String, Object>>  getActiveVariants(Pageable pageable) {
        Page<VariantListDTO> variantPage = variantService.getActiveVariants(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("variants", variantPage.getContent());
        response.put("currentPage", variantPage.getNumber());
        response.put("totalItems", variantPage.getTotalElements());
        response.put("totalPages", variantPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // 🔹 Create a new product
    @PostMapping("/variants")
    public VariantEntity createProduct(@RequestBody VariantEntity variant) {
        return variantService.createProduct(variant);
    }
}
