package com.group.tks_store.product.product.controller;

import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductListDTO;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.service.ProductService;
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
@RequestMapping("/rest/product")
public class ProductRestController {

    @Autowired
    private ProductService productService;


    @PostMapping("/create")
    public void create(@RequestBody ProductCreateDTO productCreateDTO) throws ParseException {
        productService.create(productCreateDTO);
    }

    // 🔹 Get all products
    @GetMapping("/list")
    public List<ProductEntity> getAllProducts() {
        return productService.getAllProducts();
    }

    // 🔹 Get paginated active products
    @GetMapping("/products/active")
    public ResponseEntity<Map<String, Object>>  getActiveProducts(Pageable pageable) {
        Page<ProductListDTO> productPage = productService.getActiveProducts(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // 🔹 Create a new product
    @PostMapping("/products")
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productService.createProduct(product);
    }
}
