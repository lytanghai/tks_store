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
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String create(@ModelAttribute CategoryCreateDTO category) {
        categoryService.create(category);
        return "category";
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
}
