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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
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

    @GetMapping("/list/filter")
    public String getProductFilterDetail(@RequestParam(name = CommonKey.PAGE, defaultValue = "1") Integer pageNumber,
                                         @RequestParam(name = CommonKey.SIZE, defaultValue = "10") Integer pageSize,
                                         @RequestParam(name = CommonKey.SORT, defaultValue = "id") String sortBy,
                                         @RequestParam(name = CommonKey.DIRECTION, defaultValue = "DESC") String sortDirection,
                                         @RequestParam(name = "code", defaultValue = "") String code,
                                         @RequestParam(name = "product_name", defaultValue = "") String productName,
                                         @RequestParam(name = "category_name", defaultValue = "") String categoryName,
                                         @RequestParam(name = "sale_price", defaultValue = "") String salePrice,
                                         @RequestParam(name = "sale_price_val1", defaultValue = "") String salePriceVal1,
                                         @RequestParam(name = "sale_price_val2", defaultValue = "") String salePriceVal2,
                                         @RequestParam(name = "stock_quantity", defaultValue = "") String stockQty,
                                         @RequestParam(name = "stock_quantity_val1", defaultValue = "") String stockQtyVal1,
                                         @RequestParam(name = "stock_quantity_val2", defaultValue = "") String stockQtyVal2,
                                         @RequestParam(name = "sku", defaultValue = "") String sku,
                                         @RequestParam(name = "created_date", defaultValue = "") String dateTime,
                                         @RequestParam(name = "condition_type", defaultValue = "") String conditionType,
                                         Model model) {

        Map<String, Object> propertiesList = new HashMap<>();

        // Add non-empty properties to the list
        if (!code.isEmpty()) {
            propertiesList.put("code", code);
        }
        if (!productName.isEmpty()) {
            propertiesList.put("product_name", productName);
        }
        if (!categoryName.isEmpty()) {
            propertiesList.put("category_name", categoryName);
        }
        if (!salePrice.isEmpty()) {
            propertiesList.put("sale_price", salePrice);
        }
        if (!stockQty.isEmpty()) {
            propertiesList.put("stock_quantity", stockQty);
        }
        if (!sku.isEmpty()) {
            propertiesList.put("sku", sku);
        }
        if (!dateTime.isEmpty()) {
            propertiesList.put("created_date", dateTime);
        }


        // Get filtered product details from the service
        Page<ProductListDetailDTO> productPage = productService.getProductFilterDetail(PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection),
                        sortBy)), propertiesList);

        int totalPage = productPage.getTotalPages();

        model.addAttribute("page_type_en", AddressRedirect.PRODUCT);
        model.addAttribute("page_type_kh", AddressRedirect.PRODUCT_KH);
        model.addAttribute("content", productPage.getContent());
        model.addAttribute("total_records", productPage.getTotalElements());
        model.addAttribute("total_pages",  totalPage);
        model.addAttribute("current_page", productPage.getNumber());
        model.addAttribute("sort_by", sortBy);
        model.addAttribute("sort_direction", sortDirection);
        model.addAttribute("first", productPage.isFirst());
        model.addAttribute("last", productPage.isLast());

        return AddressRedirect.HOME;
    }

}
