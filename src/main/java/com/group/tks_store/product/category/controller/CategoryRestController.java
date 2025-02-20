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
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public List<CategoryEntity> list() {
        return categoryService.list();
    }
//
//    @PostMapping("/update")
//    public void update(@RequestBody CategoryUpdateDto payloadRequest) {
//        categoryService.update(payloadRequest);
//    }
//
//    @PostMapping("/delete")
//    public void delete(@RequestBody ID req) {
//        categoryService.delete(req);
//    }

//    @GetMapping("/list/pagination")
//    public CategoryListDTO getYourEntities(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "ACTIVE") String status,
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String nameKh,
//            @RequestParam(required = false) String description,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String[] sort
//    ) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort));
//
//        return categoryService.findAllByPagination(status, pageRequest);
//    }


}
