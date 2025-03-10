package com.group.tks_store.product.product.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.util.CurrencyFormatUtil;
import com.group.tks_store.common.util.ImageUtil;
import com.group.tks_store.common.util.MappingUtil;
import com.group.tks_store.product.category.dto.CategoryDetailDTO;
import com.group.tks_store.product.images.dto.ImageDTO;
import com.group.tks_store.product.product.dto.ProductListDetailDTO;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.dto.VariantDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductFilterService {

    @Autowired
    private ProductRepository productRepository;

    public Page<ProductListDetailDTO> fetchProductFilterResponse(Pageable pageable, Map<String,Object> properties) {
        List<ProductListDetailDTO> getActiveProducts = this.getProductFilterDetail(pageable, properties);

        List<ProductListDetailDTO> mappedProducts = getActiveProducts.stream().map(result -> {
            Integer productId = result.getId();
            String productNameEn = result.getNameEn();
            String productNameKh = result.getNameKh();
            String code = result.getCode();
            Double salePrice = result.getSalePrice();
            String currency = result.getCurrency();
            String description = result.getDescription();
            String status = result.getStatus();
            Date createdAt = result.getCreatedAt();
            CategoryDetailDTO category = result.getCategory();
            List<VariantDetailDTO> variants = result.getVariants();

            return new ProductListDetailDTO(productId, productNameEn, productNameKh, code, salePrice, currency, description, status, createdAt, category, variants);
        }).collect(Collectors.toList());

        return new PageImpl<>(mappedProducts, pageable, getActiveProducts.size());
    }
    public List<ProductListDetailDTO> getProductFilterDetail(Pageable pageable, Map<String,Object> properties) {
        Page<Object[]> response = this.response(properties, pageable);
        List<ProductListDetailDTO> productResponse = new ArrayList<>();

        /** 0 -> 7 = Product*/
        /** 8 -> 11 = Category*/
        /** 12 -> 16 = Variants*/
        /** 17  = AttributeName(AttributeNameKh) : VariantAttributeValue*/
        /** 18  = ImageId:UUID*/

        for (Object[] row : response) {
            Integer productId = (Integer) row[0];
            String nameEn = (String) row[1];
            String nameKh = (String) row[2];
            String code = (String) row[3];
            Double salePrice = ((Number) row[4]).doubleValue();
            String currency = (String) row[5];
            String description = (String) row[6];
            String status = (String) row[7];
            Date createdAt = (Date) row[8];

            // Mapping category
            CategoryDetailDTO category = new CategoryDetailDTO(
                    (Integer) row[9],
                    (String) row[10],
                    (String) row[11],
                    (String) row[12]
            );

            VariantDetailDTO variant = null;

            if(row[13] != null) {
                List<ImageDTO> images = new ArrayList<>();
                if(row [19] != null) {
                    images = ImageUtil.mapImageUUIDPair((String) row[19]);
                }
                if(row [17] != null) {
                    variant = new VariantDetailDTO(
                            (Integer) row[13],
                            (String) row[14],
                            ((Number) row[15]).doubleValue(),
                            (String) row[16],
                            ((Number) row[17]).intValue(),
                            images,
                            MappingUtil.mapVariantAttributes((String) row[18]));
                }

            }

            // Check if product already exists in list
            ProductListDetailDTO existingProduct = productResponse.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (existingProduct == null) {
                existingProduct = new ProductListDetailDTO(
                        productId, nameEn, nameKh, code, salePrice, currency, description, status, createdAt, category, new ArrayList<>()
                );
                productResponse.add(existingProduct);
            }
            if(variant != null) {
                existingProduct.getVariants().add(variant);
            }
        }
        return productResponse;
    }

    private Page<Object[]> response(Map<String,Object> properties, Pageable pageable) {
        String code = "";
        String sku = "";
        String variantAttributeValue = "";
        String categoryName = "";
        String productName = "";
        String salePriceCurrency = "";
        String conditionType = "";
        String fromDate = "";
        String toDate = "";
        String general = "";
        int stockQuantity = -1;
        Double salePrice = null;
        Double salePriceVal1 = null;
        Double salePriceVal2 = null;
        Integer stockQtyVal1 = null;
        Integer stockQtyVal2 = null;
        if(!properties.isEmpty()) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                switch (entry.getKey()) {
                    case "code":
                        code = entry.getValue().toString();
                        break;
                    case "product_name":
                        productName = entry.getValue().toString();
                        break;
                    case "from_date":
                        fromDate = entry.getValue().toString();
                        break;
                    case "to_date":
                        toDate = entry.getValue().toString();
                        break;
                    case "general":
                        general = entry.getValue().toString();
                        break;
                    case "category_name":
                        categoryName = entry.getValue().toString();
                        break;
                    case "sale_price_currency":
                        salePriceCurrency = entry.getValue().toString();
                        break;
                    case "sale_price":
                        salePrice = Double.valueOf(entry.getValue().toString());
                        break;
                    case "sale_price_range":
                        Double[] salePriceRange = (Double[]) entry.getValue();
                        if (salePriceRange.length == 2) {
                            salePriceVal1 = salePriceRange[0];
                            salePriceVal2 = salePriceRange[1];
                        }
                        break;
                    case "stock_quantity":
                        stockQuantity = Integer.valueOf(entry.getValue().toString());
                        break;
                    case "stock_quantity_range":
                        Integer[] stockQtyRange = (Integer[]) entry.getValue();
                        if (stockQtyRange.length == 2) {
                            stockQtyVal1 = stockQtyRange[0];
                            stockQtyVal2 = stockQtyRange[1];
                        }
                        break;
                    case "sku":
                        sku = entry.getValue().toString();
                        break;
                    case "variant_attribute_value":
                        variantAttributeValue = entry.getValue().toString();
                        break;
                    case "condition_type":
                        conditionType = entry.getValue().toString();
                        break;
                    default:
                        break;
                }
            }
        }
        Double salePriceUSD = null;
        Double salePriceKHR = null;

        if(salePrice != null) {
            if(salePriceCurrency.equals("USD")) {
                salePriceUSD = salePrice;
                salePriceKHR = CurrencyFormatUtil.convertCurrency(salePriceUSD, "USD", "KHR");
            } else {
                salePriceKHR = salePrice ;
                salePriceUSD = CurrencyFormatUtil.convertCurrency(salePriceKHR, "KHR","USD");
            }
        }

        Page<Object[]> result = null;
        switch (conditionType) {
            case "CONTAINS" :
                result = productRepository.fetchProductByPropertyUsingContains(pageable,
                        code.equals("") ? null : code,
                        productName.equals("") ? null : productName,
                        categoryName.equals("") ? null : categoryName,
                        salePriceUSD,
                        salePriceKHR,
                        salePriceCurrency,
                        sku.equals("") ? null : sku,
                        stockQuantity == -1 ? null : stockQuantity,
                        variantAttributeValue.equals("") ? null : variantAttributeValue);

                break;

            case "EQUAL" :
                result = productRepository.fetchProductByPropertyUsingEqual(
                        pageable,
                        code.equals("") ? null : code,
                        productName.equals("") ? null : productName,
                        categoryName.equals("") ? null : categoryName,
                        salePriceUSD,
                        salePriceKHR,
                        salePriceCurrency.equals("") ? null : salePriceCurrency,
                        sku.equals("") ? null : sku,
                        stockQuantity == -1 ? null : stockQuantity,
                        variantAttributeValue.equals("") ? null : variantAttributeValue);
                break;
            case "BETWEEN" :
                result = productRepository.fetchProductByPropertyUsingBetween(
                        pageable,
                        salePriceVal1,
                        salePriceVal2,
                        salePriceCurrency.equals("") ? null : salePriceCurrency,
                        stockQtyVal1,
                        stockQtyVal2);
                break;

            case "GREATER_THAN" :
                result = productRepository.fetchProductByPropertyUsingGreaterThan(
                        pageable,
                        salePriceUSD,
                        salePriceKHR,
                        salePriceCurrency.equals("") ? null : salePriceCurrency,
                        stockQuantity);
                break;

            case "LESS_THAN" :
                result = productRepository.fetchProductByPropertyUsingLessThan(
                        pageable,
                        salePriceUSD,
                        salePriceKHR,
                        salePriceCurrency.equals("") ? null : salePriceCurrency,
                        stockQuantity);
                break;

            case "defaultCondition" :
                result = productRepository.fetchProductByPropertyUsingGeneral(
                        pageable,
                        general.equals("") ? null : general);
                break;

            default:
                result = productRepository.findActiveProductsRaw(pageable);
                break;
        }
        return result;
    }
}
