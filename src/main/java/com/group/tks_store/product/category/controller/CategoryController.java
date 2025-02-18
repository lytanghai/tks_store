package com.group.tks_store.product.category.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String create(@RequestBody CategoryCreateDTO category) {
        categoryService.create(category);
        return "redirect:/category";
    }

    @PostMapping("/update")
    public void update(@RequestBody CategoryUpdateDto payloadRequest) {
        categoryService.update(payloadRequest);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody ID req) {
        categoryService.delete(req);
    }

    @GetMapping("/list")
    public List<CategoryEntity> list() {
        return categoryService.list();
    }

    @GetMapping("/list/pagination")
    public CategoryListDTO getYourEntities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "ACTIVE") String status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nameKh,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String[] sort
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort));

        return categoryService.findAllByPagination(status, pageRequest);
    }

    @GetMapping("/category")
    public String redirect2Category(Model model, @RequestParam(name = "page", defaultValue = "0") Integer pageNumber) {
//        log.info("pageNumber: {}", pageNumber);

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
        model.addAttribute("create-category", new CategoryCreateDTO());
//        log.info("size: {}", result.getRecords().size());

        return "home";
    }
}
