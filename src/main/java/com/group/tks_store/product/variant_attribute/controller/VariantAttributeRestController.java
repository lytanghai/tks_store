package com.group.tks_store.product.variant_attribute.controller;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variant-attributes")
public class VariantAttributeRestController {

    @Autowired
    private VariantAttributeService variantAttributeService;

    @PostMapping("/create")
    public VariantAttributeDTO createVariantAttribute(@RequestBody VariantAttributeDTO dto) {
        return variantAttributeService.createVariantAttribute(dto);
    }

    @GetMapping("/{id}")
    public VariantAttributeDTO getVariantAttribute(@PathVariable Integer id) {
        return variantAttributeService.getVariantAttributeById(id);
    }

    @GetMapping("/all")
    public List<VariantAttributeDTO> getAllVariantAttributes() {
        return variantAttributeService.getAllVariantAttributes();
    }

    @PutMapping("/update/{id}")
    public VariantAttributeDTO updateVariantAttribute(@PathVariable Integer id, @RequestBody VariantAttributeDTO dto) {
        return variantAttributeService.updateVariantAttribute(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteVariantAttribute(@PathVariable Integer id) {
        variantAttributeService.deleteVariantAttribute(id);
        return "Variant Attribute deleted successfully!";
    }
}