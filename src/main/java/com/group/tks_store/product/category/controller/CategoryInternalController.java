package com.group.tks_store.product.category.controller;

import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(AddressRedirect.INTERNAL + AddressRedirect.CATEGORY)
public class CategoryInternalController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(CommonKey.LIST)
    public List<CategoryEntity> list() {
        return categoryService.list();
    }


}
