package com.group.tks_store.product.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.exception.ServiceException;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryDetailDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import com.group.tks_store.product.product.dto.*;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant.dto.VariantDetailDTO;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant.service.VariantService;
import com.group.tks_store.product.variant.variant_image.dto.VariantImageCreateDTO;
import com.group.tks_store.product.variant.variant_image.service.VariantImageService;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTOV2;
import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductService {

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
    private VariantImageService imageService;

    public void create(ProductCreateDTO productCreateDTO) throws ParseException {

        CategoryEntity category = categoryRepository.findById(productCreateDTO.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category Id is not found"));

        ProductEntity product = new ProductEntity();
        product.setCategory(category);
        product.setNameEn(productCreateDTO.getNameEn());
        product.setNameKh(productCreateDTO.getNameKh());
        product.setCode(productCreateDTO.getCode());
        product.setDescription(productCreateDTO.getDescription());
        product.setCurrency(productCreateDTO.getCurrency());
        product.setSalePrice(productCreateDTO.getSalePrice());
        product.setCreatedAt(DateTimeUtil.convertDate(new Date()));
        product.setStatus(Status.ACTIVE.getValue());
        productRepository.save(product);
    }

    @org.springframework.transaction.annotation.Transactional
    public void formCreateProduct(String productJson, List<MultipartFile> images) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> productObj = objectMapper.readValue(productJson, Map.class);

        ProductFullCreateDTO newProductInformation = new ProductFullCreateDTO();
        Integer variantId = 0;
        for(Map.Entry<String,Object> key : productObj.entrySet()) {

            String keyName = key.getKey();
            if(keyName.equals("product") && Objects.nonNull(key.getValue())) {
                Map<String,Object> product = (Map<String, Object>) key.getValue();
                ProductCreateDTO productDTO = new ProductCreateDTO();
                productDTO.setNameEn(String.valueOf(product.getOrDefault("name_en", null)));
                productDTO.setNameKh(String.valueOf(product.getOrDefault("name_kh", null)));
                productDTO.setCode(String.valueOf(product.getOrDefault("code", null)));
                Integer salePriceInteger = (Integer) product.getOrDefault("sale_price", 0.0);
                Double salePrice = salePriceInteger != null ? salePriceInteger.doubleValue() : 0.0;
                productDTO.setSalePrice(salePrice);
                productDTO.setCurrency(String.valueOf(product.getOrDefault("currency", null)));
                productDTO.setDescription(String.valueOf(product.getOrDefault("description", null)));
                Map<String,Object> category = (Map<String, Object>) product.get("category");
                CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();

                String categoryIdString = (String) category.getOrDefault("id", "0");

                Integer categoryId = Integer.parseInt(categoryIdString);
                categoryCreateDTO.setId(categoryId);
                productDTO.setCategory(categoryCreateDTO);

                newProductInformation.setProduct(productDTO);
            }

            if(keyName.equals("variant") && Objects.nonNull(key.getValue())) {
                Map<String,Object> variant = (Map<String, Object>) key.getValue();
                VariantCreateDTO variantDTO = new VariantCreateDTO();
                Integer basePriceInteger = (Integer) variant.getOrDefault("base_price", 0);
                Double basePrice = basePriceInteger != null ? basePriceInteger.doubleValue() : 0.0;
                variantDTO.setBasePrice(basePrice);
                variantDTO.setCurrency((String) variant.getOrDefault("currency", null));
                variantDTO.setStockQuantity((Integer) variant.getOrDefault("stock_quantity", 0));
                variantDTO.setSku((String) variant.getOrDefault("sku", null));
                newProductInformation.setVariant(variantDTO);
            }

            if(keyName.equals("variant_images") && Objects.nonNull(key.getValue())) {
                List<VariantImageCreateDTO> listVariantImgDTO = new ArrayList<>();
                ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
                for(int i =0; i< arrayList.size();i++) {
                    Map<String,Object> map = (Map<String, Object>) arrayList.get(i);
                    VariantImageCreateDTO variantImageCreateDTO = new VariantImageCreateDTO();
                    variantImageCreateDTO.setVariantId((Integer) map.getOrDefault("variant_id", null));
                    listVariantImgDTO.add(variantImageCreateDTO);
                }
                newProductInformation.setVariantImage(listVariantImgDTO);
            }

            if(keyName.equals("variant_attributes") && Objects.nonNull(key.getValue())) {

                List<VariantAttributeDTO> listVariantAttributeDTO = new ArrayList<>();
                ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
                for(int i =0 ;i < arrayList.size();i++) {
                    Map<String,Object> map = (Map<String, Object>) arrayList.get(i);
                    VariantAttributeDTO variantAttributeDTO = new VariantAttributeDTO();
                    variantAttributeDTO.setAttributeId((Integer) map.getOrDefault("attribute_id", null));
                    variantAttributeDTO.setValue((String) map.getOrDefault("value", null));
                    listVariantAttributeDTO.add(variantAttributeDTO);
                }
                newProductInformation.setVariantAttributes(listVariantAttributeDTO);
            }

        }
        this.createFullProduct(newProductInformation, images);
    }

    public ProductEntity createProduct(ProductCreateDTO productCreateDTO) throws ParseException {

        CategoryEntity category = categoryRepository.findById(productCreateDTO.getCategory().getId()).orElseThrow(() -> new ServiceException("CT-002", "ស្វែងរកមិនឃើញទេ! លេខរៀង: " + productCreateDTO.getCategory().getId()));

        ProductEntity product = new ProductEntity();
        product.setCategory(category);
        product.setNameEn(productCreateDTO.getNameEn());
        product.setNameKh(productCreateDTO.getNameKh());
        product.setCode(productCreateDTO.getCode());
        product.setDescription(productCreateDTO.getDescription());
        product.setCurrency(productCreateDTO.getCurrency());
        product.setSalePrice(productCreateDTO.getSalePrice());
        product.setCreatedAt(DateTimeUtil.convertDate(new Date()));
        product.setStatus(Status.ACTIVE.getValue());
        return productRepository.save(product);
    }

    @org.springframework.transaction.annotation.Transactional
    public void createFullProduct(ProductFullCreateDTO productCreateDTO, List<MultipartFile> imageFile) throws ParseException, IOException {

        ProductEntity product = null;
        if(!ObjectUtils.isEmpty(productCreateDTO.getProduct())) {
            product = this.createProduct(productCreateDTO.getProduct());
        }
        assert product != null;

        VariantEntity variant = null;
        if(!ObjectUtils.isEmpty(productCreateDTO.getVariant())) {
            variant = variantService.createVariant(productCreateDTO.getVariant(), product.getId());
        }

        assert variant != null;

        if(!ObjectUtils.isEmpty(imageFile)) {
            for (MultipartFile image : imageFile) {
                imageService.create(variant.getId(), image);
            }
        }

        if(!ObjectUtils.isEmpty(productCreateDTO.getVariantAttribute())) {
            for (VariantAttributeDTO variantAttributeDTO : productCreateDTO.getVariantAttribute()) {
                variantAttributeService.createVariantAttribute2(variantAttributeDTO, variant.getId());
            }
        }
    }

    public void update(ProductUpdateDto payloadRequest) throws ParseException {
        ProductEntity existProduct = productRepository.getById(payloadRequest.getId());
        if(Objects.nonNull(existProduct)) {
            existProduct.setSalePrice(payloadRequest.getSalePrice());
            existProduct.setCurrency(payloadRequest.getCurrency());
            existProduct.setNameEn(payloadRequest.getNameEn());
            existProduct.setNameKh(payloadRequest.getNameKh());
            existProduct.setCode(payloadRequest.getCode());
            existProduct.setDescription(payloadRequest.getDescription());
            existProduct.setLastUpdatedAt(DateTimeUtil.convertDate(new Date()));
            this.productRepository.save(existProduct);
        }
    }

    public void delete(ID req) {
        productRepository.deleteById(req.getId());
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Page<ProductListDTO> getActiveProducts(Pageable pageable) {
        return productRepository.findByStatus("ACTIVE", pageable).map(result -> {
            Integer id = (Integer) result[0];
            String nameEn = (String) result[1];
            String nameKh = (String) result[2];
            String code = (String) result[3];
            BigDecimal salePrice = (BigDecimal) result[4];
            String currency = (String) result[5];
            String description = (String) result[6];
            String status = (String) result[7];
            Date createdAt = (Date) result[8];
            Date lastUpdatedAt = (Date) result[9];
            String categoryName = (String) result[10];
            String categoryNameKh = (String) result[11];

            Double salePriceDouble = salePrice != null ? salePrice.doubleValue() : null;

            return new ProductListDTO(id, nameEn, nameKh, code, salePriceDouble, currency, description, status, createdAt, lastUpdatedAt, categoryName, categoryNameKh);
        });
    }

    @Transactional
    public ProductEntity createProduct(ProductEntity product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            CategoryEntity category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    public List<ProductListDetailDTO> getActiveProducts() {
        List<Object[]> results = productRepository.findActiveProductsRaw();
        List<ProductListDetailDTO> products = new ArrayList<>();

        for (Object[] row : results) {
            Integer productId = (Integer) row[0];
            String nameEn = (String) row[1];
            String nameKh = (String) row[2];
            String code = (String) row[3];
            Double salePrice = ((Number) row[4]).doubleValue();
            String currency = (String) row[5];
            String description = (String) row[6];
            String status = (String) row[7];

            // Mapping category
            CategoryDetailDTO category = new CategoryDetailDTO(
                    ((Number) row[8]).longValue(),
                    (String) row[9],
                    (String) row[10],
                    (String) row[11]
            );

            // Mapping variants
            VariantDetailDTO variant = new VariantDetailDTO(
                    ((Number) row[12]).longValue(),
                    (String) row[13],
                    ((Number) row[14]).doubleValue(),
                    (String) row[15],
                    ((Number) row[16]).intValue(),
                    row[17] != null ? List.of((String[]) row[17]) : new ArrayList<>(),
                    mapVariantAttributes((String[]) row[18])
            );

            // Check if product already exists in list
            ProductListDetailDTO existingProduct = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (existingProduct == null) {
                existingProduct = new ProductListDetailDTO(
                        productId, nameEn, nameKh, code, salePrice, currency, description, status, category, new ArrayList<>()
                );
                products.add(existingProduct);
            }

            existingProduct.getVariants().add(variant);
        }

        return products;
    }

    private List<VariantAttributeDTOV2> mapVariantAttributes(String[] attributes) {
        List<VariantAttributeDTOV2> variantAttributes = new ArrayList<>();
        if (attributes != null) {
            for (String attr : attributes) {
                String[] parts = attr.split(":");
                if (parts.length == 2) {
                    variantAttributes.add(new VariantAttributeDTOV2(Integer.valueOf(parts[0]), parts[1]));
                }
            }
        }
        return variantAttributes;
    }

    @Transactional
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
