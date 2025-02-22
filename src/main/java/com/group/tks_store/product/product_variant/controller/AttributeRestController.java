package com.group.tks_store.product.product_variant.controller;

import com.group.tks_store.product.product_variant.entity.AttributeEntity;
import com.group.tks_store.product.product_variant.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/attribute")
public class AttributeRestController {

    @Autowired
    private AttributeService attributeService;

    @GetMapping("/list")
    public List<AttributeEntity> list() {
        return attributeService.list();
    }

}
