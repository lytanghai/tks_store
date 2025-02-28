package com.group.tks_store.product.variant.variant_image.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.variant.service.VariantService;
import com.group.tks_store.product.variant.variant_image.dto.VariantImageListDTO;
import com.group.tks_store.product.variant.variant_image.service.VariantImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping("/variant-image")
public class VariantImageController {

    @Autowired
    private VariantService variantService;

    @Autowired
    private VariantImageService variantImageService;

    @PostMapping("/create")
    public String create(@RequestParam("variant_id") Integer variantId,
                         @RequestParam("image") MultipartFile image) throws ParseException, IOException {
        variantImageService.create(variantId, image);
        return "redirect:/variant-image";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        variantImageService.delete(id);
        return "redirect:/variant-image";
    }

    @PostMapping("/update")
    public String delete(@RequestParam("variant_image_id") Integer variantImageId,
                         @RequestParam("variant_id") Integer variantId,
                         @RequestParam("image") MultipartFile image) throws ParseException, IOException {
        variantImageService.update(variantImageId, variantId,image);
        return "redirect:/variant-image";
    }

    @GetMapping("/list")
    public String getActiveVariantPages(@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
                                    @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
                                    @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                    @RequestParam(name = "direction", defaultValue = "DESC") String sortDirection,
                                    Model model) {
        System.out.println("Calling Variant List API");

        Page<VariantImageListDTO> variantPage = variantImageService.getActiveVariantImage(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.fromString(sortDirection),
                        sortBy))
        );

        model.addAttribute("page_type_en", "variant");
        model.addAttribute("page_type_kh", "វ៉ារ្យ៉ង់");
        model.addAttribute("content", variantPage.getContent());
        model.addAttribute("total_records", variantPage.getTotalElements());
        model.addAttribute("total_pages", variantPage.getTotalPages());
        model.addAttribute("current_page", variantPage.getNumber());
        model.addAttribute("sort_by", sortBy);
        model.addAttribute("sort_direction", sortDirection);
        model.addAttribute("first", variantPage.isFirst());
        model.addAttribute("last", variantPage.isLast());

        return "home";
    }


}
