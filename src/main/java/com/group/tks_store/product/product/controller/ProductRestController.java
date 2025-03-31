package com.group.tks_store.product.product.controller;

import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.common.static_key.LIB;
import com.group.tks_store.common.util.Transform;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductListDetailDTO;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.service.ProductService;
import com.group.tks_store.product.product.service.ProductServiceBK;
import com.group.tks_store.product.product.service.ProductFilterService;
import org.json.JSONArray;
import org.json.JSONObject;
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
    private ProductFilterService productFilterService;

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
    public ResponseEntity<Object> getProductFilterDetail(@RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber,
                                         @RequestParam(name = CommonKey.SIZE, defaultValue = "10") Integer pageSize,
                                         @RequestParam(name = CommonKey.SORT, defaultValue = "id") String sortBy,
                                         @RequestParam(name = CommonKey.DIRECTION, defaultValue = "DESC") String sortDirection,
                                         @RequestParam(name = "code", defaultValue = "") String code,
                                         @RequestParam(name = "product_name", defaultValue = "") String productName,
                                         @RequestParam(name = LIB.category_id, defaultValue = "-1") Integer categoryId,
                                         @RequestParam(name = LIB.category_name, defaultValue = "") String categoryName,
                                         @RequestParam(name = "sale_price", defaultValue = "") String salePrice,
                                         @RequestParam(name = "sale_price_val1", defaultValue = "") String salePriceVal1,
                                         @RequestParam(name = "sale_price_val2", defaultValue = "") String salePriceVal2,
                                         @RequestParam(name = "sale_price_currency", defaultValue = "") String salePriceCurrency,
                                         @RequestParam(name = "stock_quantity", defaultValue = "") String stockQty,
                                         @RequestParam(name = "stock_quantity_val1", defaultValue = "") String stockQtyVal1,
                                         @RequestParam(name = "stock_quantity_val2", defaultValue = "") String stockQtyVal2,
                                         @RequestParam(name = "sku", defaultValue = "") String sku,
                                         @RequestParam(name = "variant_attribute_value", defaultValue = "") String variantAttributeValue,
                                         @RequestParam(name = "general", defaultValue = "") String general,
                                         @RequestParam(name = "condition_type", defaultValue = "") String conditionType,
                                         Model model) {

        Map<String, Object> propertiesList = this.mapPropertyList(
                code, productName, categoryId, categoryName, salePrice, stockQty, sku, variantAttributeValue, general,salePriceCurrency,salePriceVal1,salePriceVal2,stockQtyVal1,stockQtyVal2,conditionType);

        pageNumber -=1;
        // Get filtered product details from the service
        Page<ProductListDetailDTO> productPage = productFilterService.fetchProductFilterResponse(PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy)),
                propertiesList);

        int totalPage = productPage.getTotalPages();

        if(pageNumber == 0) {
//            totalPage += 1;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber() + 1);
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", totalPage);

        log.info("calling to get filter");

        JSONObject jsonObject = new JSONObject(transformResponse(response));
        JSONObject snakeCaseJson = Transform.convertKeysToSnakeCase(jsonObject);

        return ResponseEntity.ok(snakeCaseJson.toString(2));
    }

    public Map<String, Object> transformResponse(Map<String, Object> response) {
        JSONObject input = new JSONObject(response);

        JSONArray products = input.getJSONArray("products");

        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            JSONArray variants = product.getJSONArray("variants");

            JSONArray combinedAttributes = new JSONArray();

            for (int j = 0; j < variants.length(); j++) {
                JSONObject variant = variants.getJSONObject(j);
                JSONArray attributes = variant.getJSONArray("attributes");

                for (int k = 0; k < attributes.length(); k++) {
                    combinedAttributes.put(attributes.get(k));
                }
            }

            JSONArray newVariants = new JSONArray();
            JSONObject mergedVariant = new JSONObject();
            mergedVariant.put("id", variants.getJSONObject(0).getInt("id"));
            mergedVariant.put("sku", variants.getJSONObject(0).getString("sku"));
            mergedVariant.put("base_price", variants.getJSONObject(0).getDouble("basePrice"));
            mergedVariant.put("base_price_currency", variants.getJSONObject(0).getString("basePriceCurrency"));
            mergedVariant.put("stock_quantity", variants.getJSONObject(0).getInt("stockQuantity"));
            mergedVariant.put("images", variants.getJSONObject(0).getJSONArray("images"));
            mergedVariant.put("attributes", combinedAttributes);
            newVariants.put(mergedVariant);

            product.put("variants", newVariants);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalItems", input.get("totalItems"));
        resultMap.put("totalPages", input.get("totalPages"));
        resultMap.put("currentPage", input.get("currentPage"));
        resultMap.put("products", input.getJSONArray("products").toList());  // Convert products JSONArray to a list

        return resultMap;
    }

    private Map<String,Object> mapPropertyList(String code, String productName, Integer categoryId, String categoryName, String salePrice, String stockQty, String sku,
                                               String variantAttributeValue, String general, String salePriceCurrency, String salePriceVal1, String salePriceVal2,
                                               String stockQtyVal1, String stockQtyVal2, String conditionType) {
        Map<String, Object> propertiesList = new HashMap<>();

        if (!code.isEmpty()) {
            propertiesList.put("code", code);
        }
        if (!productName.isEmpty()) {
            propertiesList.put("product_name", productName);
        }
        if (categoryId != -1) {
            propertiesList.put(LIB.category_id, categoryId);
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
        if (!variantAttributeValue.isEmpty()) {
            propertiesList.put("variant_attribute_value", variantAttributeValue);
        }

        if (!general.isEmpty()) {
            propertiesList.put("general", general);
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
