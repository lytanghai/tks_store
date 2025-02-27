package com.group.tks_store.product.variant_attribute.controller;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeListDTO;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VariantAttributeController {

    @Autowired
    private VariantAttributeService variantAttributeService;

    @GetMapping("/list")
    public String getVariantAttributes(Model model,
                                       @RequestParam(name = "page", defaultValue = "0") Integer pageNumber) {

        System.out.println("Calling Variant Attribute List API");

        VariantAttributeListDTO result = variantAttributeService.findAllByPagination(
                "ACTIVE",
                PageRequest.of(pageNumber, 10, Sort.Direction.DESC, "id")
        );

        model.addAttribute("page_type_en", "variant attribute");
        model.addAttribute("page_type_kh", "លក្ខណៈសម្បត្តិ Variant");
        model.addAttribute("content", result.getRecords());
        model.addAttribute("total_records", result.getTotalRecords());
        model.addAttribute("total_pages", result.getTotalPages());
        model.addAttribute("current_page", result.getPageNumber());
        model.addAttribute("sort_by", result.getSortBy());
        model.addAttribute("sort_direction", result.getSortDirection());
        model.addAttribute("first", result.getFirst());
        model.addAttribute("last", result.getLast());

        return "home";
    }
}
