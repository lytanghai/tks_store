package com.group.tks_store.product.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.common.static_key.LIB;
import com.group.tks_store.product.category.dto.CategoryDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import com.group.tks_store.product.product.dto.ProductListDetailDTO;
import com.group.tks_store.product.product.service.ProductFilterService;
import com.group.tks_store.product.product.service.ProductService;
import com.group.tks_store.product.product.service.ProductServiceBK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(CommonKey.API_CONTEXT_PATH + AddressRedirect.PRODUCT)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductServiceBK productServiceBk;

    @Autowired
    private ProductFilterService productFilterService;

    @Autowired
    private CategoryService categoryService;

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/upload")
    public String uploadProduct(
            @RequestPart("data") String productJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException, ParseException {

            productService.createProduct(productJson, images);

        return "/fragments/" + AddressRedirect.PRODUCT;
    }

    @PostMapping(CommonKey.UPDATE)
    public String update(@RequestPart("data") String productJson,
                           @RequestPart(value = "images", required = false) MultipartFile[] images) throws JsonProcessingException {
        productService.updateProduct(productJson, images);
        return "/fragments/" + AddressRedirect.PRODUCT;
    }


    @PostMapping(CommonKey.DELETE)
    public String delete(@RequestBody ID id) {
        productService.delete(id);
        log.info("product {} deleted", id);
        return AddressRedirect.REDIRECT_PRODUCT;
    }

    @GetMapping("/api/categories")
    @ResponseBody
    public List<CategoryDTO> getCategories() {
        List<CategoryDTO> list = new ArrayList<>();
        List<CategoryEntity> listEntity = new ArrayList<>();

        for(int i=0;i<listEntity.size();i++) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(listEntity.get(i).getId());
            categoryDTO.setName(listEntity.get(i).getName());
            list.set(i, categoryDTO);
        }
        return list;
    }

    @GetMapping(CommonKey.LIST + "/filter")
    public String getProductFilterDetail(@RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber,
                                         @RequestParam(name = CommonKey.SIZE, defaultValue = "10") Integer pageSize,
                                         @RequestParam(name = CommonKey.SORT, defaultValue = "id") String sortBy,
                                         @RequestParam(name = CommonKey.DIRECTION, defaultValue = "DESC") String sortDirection,
                                         @RequestParam(name = LIB.code, defaultValue = "") String code,
                                         @RequestParam(name = LIB.product_name, defaultValue = "") String productName,
                                         @RequestParam(name = LIB.category_name, defaultValue = "") String categoryName,
                                         @RequestParam(name = LIB.category_id, defaultValue = "-1") Integer categoryId,
                                         @RequestParam(name = LIB.sale_price, defaultValue = "") String salePrice,
                                         @RequestParam(name = "sale_price_val1", defaultValue = "") String salePriceVal1,
                                         @RequestParam(name = "sale_price_val2", defaultValue = "") String salePriceVal2,
                                         @RequestParam(name = "sale_price_currency", defaultValue = "") String salePriceCurrency,
                                         @RequestParam(name = LIB.stock_quantity, defaultValue = "") String stockQty,
                                         @RequestParam(name = "stock_quantity_val1", defaultValue = "") String stockQtyVal1,
                                         @RequestParam(name = "stock_quantity_val2", defaultValue = "") String stockQtyVal2,
                                         @RequestParam(name = LIB.sku, defaultValue = "") String sku,
                                         @RequestParam(name = LIB.variant_attribute_value, defaultValue = "") String variantAttributeValue,
                                         @RequestParam(name = "condition_type", defaultValue = "") String conditionType,
                                         Model model) {

        Map<String, Object> propertiesList = this.mapPropertyList(
                code, productName, categoryId, categoryName, salePrice, stockQty, sku, variantAttributeValue, salePriceCurrency, salePriceVal1, salePriceVal2, stockQtyVal1, stockQtyVal2, conditionType);

        Page<ProductListDetailDTO> productPage = productFilterService.fetchProductFilterResponse(PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.fromString(sortDirection), sortBy)),
                propertiesList);

        model.addAttribute("page_type_en", AddressRedirect.PRODUCT);
        model.addAttribute("page_type_kh", AddressRedirect.PRODUCT_KH);
        model.addAttribute("content", productPage.getContent());
        model.addAttribute("total_records", productPage.getTotalElements());
        model.addAttribute("total_pages",  productPage.getTotalPages());
        model.addAttribute("current_page", productPage.getNumber());
        model.addAttribute("sort_by", sortBy);
        model.addAttribute("sort_direction", sortDirection);
        model.addAttribute("first", productPage.isFirst());
        model.addAttribute("last", productPage.isLast());

        return AddressRedirect.HOME;
    }

    private Map<String,Object> mapPropertyList(String code, String productName, Integer categoryId, String categoryName, String salePrice, String stockQty, String sku,
                                               String variantAttributeValue, String salePriceCurrency, String salePriceVal1, String salePriceVal2,
                                               String stockQtyVal1, String stockQtyVal2, String conditionType) {
        Map<String, Object> propertiesList = new HashMap<>();

        if (!code.isEmpty()) {
            propertiesList.put(LIB.code, code);
        }
        if (!productName.isEmpty()) {
            propertiesList.put(LIB.product_name, productName);
        }
        if (categoryId != -1) {
            propertiesList.put(LIB.category_id, categoryId);
        }
        if (!categoryName.isEmpty()) {
            propertiesList.put(LIB.category_name, categoryName);
        }
        if (!salePrice.isEmpty()) {
            propertiesList.put(LIB.sale_price, salePrice);
        }
        if (!stockQty.isEmpty()) {
            propertiesList.put(LIB.stock_quantity, stockQty);
        }
        if (!sku.isEmpty()) {
            propertiesList.put(LIB.sku, sku);
        }
        if (!variantAttributeValue.isEmpty()) {
            propertiesList.put(LIB.variant_attribute_value, variantAttributeValue);
        }
        if (!salePriceCurrency.isEmpty()) {
            propertiesList.put("sale_price_currency", salePriceCurrency);
        }
        if (!salePriceVal1.isEmpty() && !salePriceVal2.isEmpty()) {
            try {
                propertiesList.put("sale_price_range", new Double[]{
                        Double.parseDouble(salePriceVal1),
                        Double.parseDouble(salePriceVal2)
                });
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid sale_price range values");
            }
        }
        if (!stockQtyVal1.isEmpty() && !stockQtyVal2.isEmpty()) {
            try {
                propertiesList.put("stock_quantity_range", new Integer[]{
                        Integer.parseInt(stockQtyVal1),
                        Integer.parseInt(stockQtyVal2)
                });
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid stock_quantity range values");
            }
        }

        if (!conditionType.isEmpty()) {
            propertiesList.put("condition_type", conditionType);
        }

        return propertiesList;
    }

}
