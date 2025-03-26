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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductUtil {

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
            productCreateDTO.setId(Integer.valueOf(String.valueOf(product.getOrDefault(LIB.id, 0))));
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
            String categoryIdString = (String) category.getOrDefault(LIB.id, "0");
            Integer categoryId = Integer.parseInt(categoryIdString);
            categoryCreateDTO.setId(categoryId);
            productCreateDTO.setCategory(categoryCreateDTO);
            productDTO.setProduct(productCreateDTO);
        } else {
            ProductEntity existProduct = productRepository.findById(Integer.valueOf((String) product.get(LIB.id))).get();
            if(existProduct != null) {
                existProduct.setNameEn(String.valueOf(product.get(LIB.name_en)) == null ? null : String.valueOf(product.get(LIB.name_en)));
                existProduct.setNameKh(String.valueOf(product.get(LIB.name_kh)) == null ? null : String.valueOf(product.get(LIB.name_kh)));
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
                    String categoryIdString = (String) category.getOrDefault(LIB.id, "0");
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

    public void mappingVariantInfo( ProductDTO productDTO, JSONObject variantJson, String action) {
        if(action.equals(LIB.CREATE)) {
            VariantCreateDTO variantDTO = new VariantCreateDTO();
            double basePrice = variantJson.optDouble(LIB.base_price, 0.0);
            variantDTO.setBasePrice(basePrice);
            variantDTO.setCurrency(variantJson.optString(LIB.currency, ""));
            variantDTO.setStockQuantity(variantJson.optInt(LIB.stock_quantity, -99));
            variantDTO.setSku(variantJson.optString(LIB.sku, "N/A"));
            productDTO.setVariant(variantDTO);
        } else {
            if(variantJson.opt(LIB.id) != null) {
                VariantEntity existVariant = variantRepository.findById(variantJson.getInt(LIB.id)).orElse(null);
                if(existVariant != null) {
                    double basePrice;
                    try {
                        basePrice = variantJson.optDouble(LIB.base_price,0.0);
                    }catch (Exception e) {
                        basePrice = variantJson.optInt(LIB.base_price, 0);
                    }
                    existVariant.setBasePrice(basePrice);
                    existVariant.setCurrency(variantJson.optString(LIB.currency));
                    existVariant.setStockQuantity(variantJson.optInt(LIB.stock_quantity));
                    existVariant.setSku(variantJson.optString(LIB.sku));
                    variantRepository.save(existVariant);
                }
            }
        }
    }

    public void mappingVariantImgInfo(Map.Entry<String,Object> key, ProductDTO productDTO, JSONArray imagesRemove, Integer variantId, MultipartFile[] image, String action) {
        if(action.equals(LIB.CREATE)) {
            List<VariantImageCreateDTO> listVariantImgDTO = new ArrayList<>();
            ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
            for (Object o : arrayList) {
                Map<String, Object> map = (Map<String, Object>) o;
                VariantImageCreateDTO variantImageCreateDTO = new VariantImageCreateDTO();
                variantImageCreateDTO.setVariantId((Integer) map.getOrDefault(LIB.variant_id, null));
                listVariantImgDTO.add(variantImageCreateDTO);
            }
            productDTO.setVariantImage(listVariantImgDTO);
        } else {
            if(imagesRemove != null) {
                List<String> ids = new ArrayList<>();
                imagesRemove.iterator().forEachRemaining(i -> {
                    String[] each = String.valueOf(i).split("uuid=");
                    ids.add(each[1]);
                });
                imageRepository.deleteByUuid(ids);
            }
            if(!ObjectUtils.isEmpty(image)) {
                imageService.uploadMultiFiles(image, variantId);
            }
        }
    }

    public void mappingVariantAttributeInfo(Map.Entry<String,Object> key, ProductDTO productDTO, Integer productId, String action) {
        List<VariantAttributeDTO> listVariantAttributeDTO = new ArrayList<>();
        ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
        Set<Integer> existingDBId = new HashSet<>();
        Set<Integer> requestId = new HashSet<>();

        List<Integer> idsToRemove = new ArrayList<>();

        VariantEntity variant = variantRepository.findByProductId(productId);
        if(arrayList.isEmpty()) {
            variantAttributeService.removeByVariantId(variant.getId());
            return;
        }
        for (Object o : arrayList) {
            Map<String, Object> map = (Map<String, Object>) o;
            if (action.equals(LIB.UPDATE)) {
                List<VariantAttributeDetailList> variantAttributeEntities = variantAttributeService.findByVariantId(variant.getId());
                if (map.containsKey(LIB.attribute_id)) {
                    VariantAttributeDTO variantAttributeDTO = new VariantAttributeDTO();
                    variantAttributeDTO.setVariantId(variant.getId());
                    variantAttributeDTO.setAttributeId(Integer.valueOf(String.valueOf(map.getOrDefault(LIB.attribute_id, null))));
                    variantAttributeDTO.setValue((String) map.getOrDefault(LIB.value, null));
                    variantAttributeDTO.setGroupNum(Integer.valueOf(String.valueOf(map.getOrDefault("group_num", 0))));
                    variantAttributeService.createVariantAttribute2(variantAttributeDTO, variant.getId());
                }

                if (map.containsKey(LIB.id)) {
                    if (map.get(LIB.id).equals("")) {
                        continue;
                    } else {
                        requestId.add(Integer.valueOf((String) map.get(LIB.id)));
                        variantAttributeEntities.forEach(item -> existingDBId.add(item.getVariantAttributeId()));
                    }
                }
            } else if (action.equals(LIB.CREATE)) {
                VariantAttributeDTO variantAttributeDTO = new VariantAttributeDTO();
                variantAttributeDTO.setAttributeId(Integer.valueOf(String.valueOf(map.getOrDefault(LIB.attribute_id, null))));
                variantAttributeDTO.setValue((String) map.getOrDefault(LIB.value, null));
                variantAttributeDTO.setGroupNum(Integer.valueOf(String.valueOf(map.getOrDefault("group_num", 0))));
                listVariantAttributeDTO.add(variantAttributeDTO);
                productDTO.setVariantAttributes(listVariantAttributeDTO);
            }
        }

        if(action.equals(LIB.UPDATE)) {
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
