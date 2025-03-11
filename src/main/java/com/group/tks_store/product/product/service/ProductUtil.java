package com.group.tks_store.product.product.service;


import com.group.tks_store.common.static_key.LIB;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import com.group.tks_store.product.images.repository.ImageRepository;
import com.group.tks_store.product.images.service.ImageService;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductDTO;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant.service.VariantService;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDetailList;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import com.group.tks_store.product.variant_image.dto.VariantImageCreateDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductUtil {

    private static final String ATTRIBUTE_ID = "attribute_id";
    private static final String VARIANT_ID = "variant_id";
    private static final String UPDATE = "UPDATE";
    private static final String CREATE = "CREATE";
    private static final String VALUE = "value";
    private static final String ID = "id";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private VariantService variantService;

    @Autowired
    private VariantAttributeService variantAttributeService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;
    public void mappingProductInfo(Map.Entry<String,Object> key, ProductDTO productDTO, JSONObject productJson, String action) {
        Map<String,Object> product = (Map<String, Object>) key.getValue();
        if(action.equals(LIB.CREATE)) {
            ProductCreateDTO productCreateDTO = new ProductCreateDTO();
            productCreateDTO.setId(Integer.valueOf(String.valueOf(product.getOrDefault(ID, 0))));
            productCreateDTO.setNameEn(String.valueOf(product.getOrDefault(LIB.name_en, null)));
            productCreateDTO.setNameKh(String.valueOf(product.getOrDefault(LIB.name_kh, null)));
            productCreateDTO.setCode(String.valueOf(product.getOrDefault(LIB.code, null)));
            Double salePriceInteger = Double.valueOf(String.valueOf(product.getOrDefault(LIB.sale_price, 0.0)));
            Double salePrice = salePriceInteger != null ? salePriceInteger.doubleValue() : 0.0;
            productCreateDTO.setSalePrice(salePrice);
            productCreateDTO.setCurrency(String.valueOf(product.getOrDefault(LIB.currency, null)));
            productCreateDTO.setDescription(String.valueOf(product.getOrDefault(LIB.description, null)));
            Map<String,Object> category = (Map<String, Object>) product.get(LIB.category);
            CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();
            String categoryIdString = (String) category.getOrDefault(ID, "0");
            Integer categoryId = Integer.parseInt(categoryIdString);
            categoryCreateDTO.setId(categoryId);
            productCreateDTO.setCategory(categoryCreateDTO);
            productDTO.setProduct(productCreateDTO);
        } else {
            ProductEntity existProduct =  productRepository.findById(Integer.valueOf((String) product.get(LIB.id))).get();
            if(existProduct != null) {
                existProduct.setNameEn(String.valueOf(product.get(LIB.name_en)) == null ? null : String.valueOf(product.get(LIB.name_en)));
                existProduct.setNameEn(String.valueOf(product.get(LIB.name_kh)) == null ? null : String.valueOf(product.get(LIB.name_kh)));
                existProduct.setDescription(String.valueOf(product.get(LIB.description)) == null ? null : String.valueOf(product.get(LIB.description)));
                existProduct.setCurrency(String.valueOf(product.get(LIB.currency)) == null ? null : String.valueOf(product.get(LIB.currency)));
                existProduct.setCode(String.valueOf(product.get(LIB.code)) == null ? null : String.valueOf(product.get(LIB.code)));
                existProduct.setLastUpdatedAt(new Date());
                Double salePrice = 0.0;
                try {
                    salePrice = Double.valueOf((Integer) product.get(LIB.sale_price));
                }catch (Exception e) {
                    salePrice = product.get(LIB.sale_price) == null ? null : (Double) product.get(LIB.sale_price);
                }

                existProduct.setSalePrice(salePrice);

                Map<String,Object> category = (Map<String, Object>) product.get(LIB.category);
                if(category != null) {
                    String categoryIdString = (String) category.getOrDefault(ID, "0");
                    Integer categoryId = Integer.parseInt(categoryIdString);
                    CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
                    if(categoryEntity != null) {
                        existProduct.setCategory(categoryEntity);
                        productRepository.save(existProduct);
                    }
                }
            }
        }

    }

    public void mappingVariantInfo(Map.Entry<String,Object> key, ProductDTO productDTO, JSONObject variantJson,  String action) {
        if(action.equals(LIB.CREATE)) {
            Map<String,Object> variant = (Map<String, Object>) key.getValue();
            VariantCreateDTO variantDTO = new VariantCreateDTO();
            Double basePriceInteger = Double.valueOf(String.valueOf(variant.getOrDefault(LIB.base_price, 0.0)));
            Double basePrice = basePriceInteger != null ? basePriceInteger.doubleValue() : 0.0;
            variantDTO.setBasePrice(basePrice);
            variantDTO.setCurrency((String) variant.getOrDefault(LIB.currency, null));
            variantDTO.setStockQuantity((Integer) variant.getOrDefault(LIB.stock_quantity, 0));
            variantDTO.setSku((String) variant.getOrDefault(LIB.sku, null));
            productDTO.setVariant(variantDTO);
        } else {

        }

    }

    public void mappingVariantImgInfo(Map.Entry<String,Object> key, ProductDTO productDTO, JSONObject image,  String action) {
        if(action.equals(LIB.CREATE)) {
            List<VariantImageCreateDTO> listVariantImgDTO = new ArrayList<>();
            ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
            for (Object o : arrayList) {
                Map<String, Object> map = (Map<String, Object>) o;
                VariantImageCreateDTO variantImageCreateDTO = new VariantImageCreateDTO();
                variantImageCreateDTO.setVariantId((Integer) map.getOrDefault(VARIANT_ID, null));
                listVariantImgDTO.add(variantImageCreateDTO);
            }
            productDTO.setVariantImage(listVariantImgDTO);
        } else {

        }
    }

    public void mappingVariantAttributeInfo(Map.Entry<String,Object> key, ProductDTO productDTO, JSONObject variantAttribute, String action) {
        List<VariantAttributeDTO> listVariantAttributeDTO = new ArrayList<>();
        ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
        Set<Integer> existingDBId = new HashSet<>();
        Set<Integer> requestId = new HashSet<>();

        List<Integer> idsToRemove = new ArrayList<>();

        for (Object o : arrayList) {
            Map<String, Object> map = (Map<String, Object>) o;
            if (action.equals(UPDATE)) {
                VariantEntity variant = variantRepository.findByProductId(variantAttribute.getInt(LIB.id));

                List<VariantAttributeDetailList> variantAttributeEntities = variantAttributeService.findByVariantId(variant.getId());

                if (map.containsKey(ATTRIBUTE_ID)) {
                    VariantAttributeDTO variantAttributeDTO = new VariantAttributeDTO();
                    variantAttributeDTO.setVariantId(variant.getId());
                    variantAttributeDTO.setAttributeId(Integer.valueOf(String.valueOf(map.getOrDefault(ATTRIBUTE_ID, null))));
                    variantAttributeDTO.setValue((String) map.getOrDefault(VALUE, null));
                    variantAttributeService.createVariantAttribute2(variantAttributeDTO, variant.getId());
                }

                if (map.containsKey(ID)) {
                    requestId.add(Integer.valueOf((String) map.get(ID)));
                    variantAttributeEntities.forEach(item -> {
                        existingDBId.add(item.getVariantAttributeId());
                    });
                }
            } else if (action.equals(CREATE)) {
                VariantAttributeDTO variantAttributeDTO = new VariantAttributeDTO();
                variantAttributeDTO.setAttributeId(Integer.valueOf(String.valueOf(map.getOrDefault(ATTRIBUTE_ID, null))));
                variantAttributeDTO.setValue((String) map.getOrDefault(VALUE, null));
                listVariantAttributeDTO.add(variantAttributeDTO);
            }

            productDTO.setVariantAttributes(listVariantAttributeDTO);
        }

        if(action.equals(UPDATE)) {
            for (Integer id : existingDBId) {
                if (!requestId.contains(id)) {
                    idsToRemove.add(id);
                }
            }
            if(!idsToRemove.isEmpty()) {
                variantAttributeService.removeItems(idsToRemove);
            }
        }
    }
}
