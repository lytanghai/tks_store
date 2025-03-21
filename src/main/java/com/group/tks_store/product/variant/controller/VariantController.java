package com.group.tks_store.product.variant.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.product.service.ProductService;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant.dto.VariantListDTO;
import com.group.tks_store.product.variant.dto.VariantUpdateDto;
import com.group.tks_store.product.variant.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping("/variant")
public class VariantController {

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @PostMapping("/create")
    public String create(@RequestBody VariantCreateDTO variantCreateDTO) throws ParseException {
        variantService.create(variantCreateDTO);
        return "redirect:/variant";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        variantService.delete(id);
        return "redirect:/variant";
    }

    @PostMapping("/update")
    public String delete(@RequestBody VariantUpdateDto variantUpdateDto) throws ParseException {
        variantService.update(variantUpdateDto);
        return "redirect:/variant";
    }

//    @GetMapping("/api/categories")
//    @ResponseBody
//    public List<CategoryDTO> getCategories() {
//        List<CategoryDTO> list = new ArrayList<>();
//        List<CategoryEntity> listEntity = new ArrayList<>();
//
//        for(int i=0;i<listEntity.size();i++) {
//            CategoryDTO categoryDTO = new CategoryDTO();
//            categoryDTO.setId(listEntity.get(i).getId());
//            categoryDTO.setName(listEntity.get(i).getName());
//            list.set(i, categoryDTO);
//        }
//        return list;
//    }

    @GetMapping("/list")
    public String getActiveVariantPages(@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
                                    @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
                                    @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                    @RequestParam(name = "direction", defaultValue = "DESC") String sortDirection,
                                    Model model) {
        System.out.println("Calling Variant List API");

        Page<VariantListDTO> variantPage = variantService.getActiveVariants(
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
