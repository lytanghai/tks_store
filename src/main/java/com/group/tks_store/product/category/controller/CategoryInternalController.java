package com.group.tks_store.product.category.controller;

import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/category")
public class CategoryInternalController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public List<CategoryEntity> list() {
        return categoryService.list();
    }
}
