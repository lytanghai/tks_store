package com.group.tks_store.product.product.controller;

import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductListDetailDTO;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.service.ProductService;
import com.group.tks_store.product.product.service.ProductServiceBK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(AddressRedirect.INTERNAL + AddressRedirect.PRODUCT)
public class ProductRestController {

    private final Logger log = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductServiceBK productServiceBk;

    @PostMapping(CommonKey.CREATE)
    public void create(@RequestBody ProductCreateDTO productCreateDTO) throws ParseException {
        log.info("product created");
        productServiceBk.create(productCreateDTO);
    }

    // 🔹 Get all products
    @GetMapping(CommonKey.LIST)
    public List<ProductEntity> getAllProducts() {
        log.info("product listed");
        return productService.getAllProducts();
    }

    @GetMapping("/products/active/v2")
    public ResponseEntity<Map<String, Object>> getActiveProducts(Pageable pageable) {


        Page<ProductListDetailDTO> productPage = productService.getProductDetail(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        log.info("product list active");
        return ResponseEntity.ok(response);
    }

}
