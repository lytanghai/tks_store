package com.group.tks_store.product.attribute.controller;

import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.attribute.dto.AttributeDTO;
import com.group.tks_store.product.attribute.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(AddressRedirect.INTERNAL + AddressRedirect.ATTRIBUTE)
public class AttributeRestController {

    @Autowired
    private AttributeService attributeService;

    @GetMapping(CommonKey.LIST)
    public List<AttributeDTO> list() {
        return attributeService.list();
    }

}
