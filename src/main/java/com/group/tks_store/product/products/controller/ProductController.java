package com.group.tks_store.product.products.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.products.dto.ProductCreateDTO;
import com.group.tks_store.product.products.dto.ProductListDTO;
import com.group.tks_store.product.products.dto.ProductUpdateDto;
import com.group.tks_store.product.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public String create(@RequestBody ProductCreateDTO productCreateDTO) throws ParseException {
        productService.create(productCreateDTO);
        return "redirect:/product";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        productService.delete(id);
        return "redirect:/product";
    }

    @PostMapping("/update")
    public String delete(@RequestBody ProductUpdateDto productUpdateDto) throws ParseException {
        productService.update(productUpdateDto);
        System.out.println("1 UPDATED");
        return "redirect:/product";
    }

    @GetMapping("/list")
    public String redirect2Category(Model model, @RequestParam(name = "page", defaultValue = "0") Integer pageNumber) {

        ProductListDTO result = productService.findAllByPagination("ACTIVE", PageRequest.of(pageNumber, 10, Sort.Direction.DESC, "id"));

        model.addAttribute("page_type_en", "product");
        model.addAttribute("page_type_kh", "ផលិតផល");
        model.addAttribute("content", result.getRecords());
        model.addAttribute("total_records", result.getRecords().size());
        model.addAttribute("total_pages", result.getTotalPages());
        model.addAttribute("current_page", result.getPageNumber());
        model.addAttribute("sort_by", result.getSortBy());
        model.addAttribute("sort_direction", result.getSortDirection());
        model.addAttribute("first", result.getFirst());
        model.addAttribute("last", result.getLast());

        return "home";
    }

}
