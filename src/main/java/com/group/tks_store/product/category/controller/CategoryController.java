package com.group.tks_store.product.category.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public void create(@RequestBody CategoryCreateDTO payloadRequest) {
        categoryService.create(payloadRequest);
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
}
