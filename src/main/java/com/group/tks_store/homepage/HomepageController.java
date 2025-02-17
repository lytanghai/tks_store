package com.group.tks_store.homepage;

import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomepageController {
    private final Logger log = LoggerFactory.getLogger(HomepageController.class);

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page_type_kh", "ទំព័រដើម");
        model.addAttribute("page_type_en", "product");
        return "home";
    }

    @GetMapping("/category/create-form")
    public String showCreateForm(Model model) {
        model.addAttribute("create-category", new CategoryCreateDTO());
        return "fragments/form/create-category";
    }

    @GetMapping("/category")
    public String redirect2Category(Model model, @RequestParam(name = "page", defaultValue = "0") Integer pageNumber) {
        log.info("pageNumber: {}", pageNumber);

        CategoryListDTO result = categoryService.findAllByPagination("ACTIVE", PageRequest.of(pageNumber, 10, Sort.Direction.DESC, "id"));

        model.addAttribute("page_type_en", "category");
        model.addAttribute("page_type_kh", "ប្រភេទទំនិញ");
        model.addAttribute("content", result.getRecords());
        model.addAttribute("total_records", result.getRecords());
        model.addAttribute("total_pages", result.getTotalPages());
        model.addAttribute("current_page", result.getPageNumber());
        model.addAttribute("sort_by", result.getSortBy());
        model.addAttribute("sort_direction", result.getSortDirection());
        model.addAttribute("first", result.getFirst());
        model.addAttribute("last", result.getLast());
        log.info("size: {}", result.getRecords().size());

        return "home";
    }
}
