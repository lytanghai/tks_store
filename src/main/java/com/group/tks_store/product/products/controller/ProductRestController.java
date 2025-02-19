package com.group.tks_store.product.products.controller;

import com.group.tks_store.product.products.dto.ProductCreateDTO;
import com.group.tks_store.product.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/rest/product")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public void create(@RequestBody ProductCreateDTO productCreateDTO) throws ParseException {
        productService.create(productCreateDTO);
    }
}
