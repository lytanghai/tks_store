package com.group.tks_store.product.category.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;


@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String create(@RequestBody CategoryCreateDTO category) throws ParseException {
        categoryService.create(category);
        return "redirect:/category";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        categoryService.delete(id);
        return "redirect:/category";
    }

    @PostMapping("/update")
    public String delete(@RequestBody CategoryUpdateDto categoryUpdateDto) throws ParseException {
        categoryService.update(categoryUpdateDto);
        System.out.println("1 UPDATED");
        return "redirect:/category";
    }

    @GetMapping("/list")
    public String redirect2Category(Model model, @RequestParam(name = "page", defaultValue = "0") Integer pageNumber) {

        System.out.println("Calling Category List API");

        CategoryListDTO result = categoryService.findAllByPagination("ACTIVE", PageRequest.of(pageNumber, 10, Sort.Direction.DESC, "id"));

        model.addAttribute("page_type_en", "category");
        model.addAttribute("page_type_kh", "ប្រភេទទំនិញ");
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
