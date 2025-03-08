package com.group.tks_store.product.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Condition;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.CurrencyFormatUtil;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.common.util.ImageUtil;
import com.group.tks_store.common.util.MappingUtil;
import com.group.tks_store.exception.ServiceException;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryDetailDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import com.group.tks_store.product.images.dto.ImageDTO;
import com.group.tks_store.product.images.repository.ImageRepository;
import com.group.tks_store.product.images.service.ImageService;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductDTO;
import com.group.tks_store.product.product.dto.ProductListDetailDTO;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant.dto.VariantDetailDTO;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant.service.VariantService;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import com.group.tks_store.product.variant_image.dto.VariantImageCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @org.springframework.transaction.annotation.Transactional
    public void updateProduct(String productJson, MultipartFile[] images) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> productObj = objectMapper.readValue(productJson, Map.class);

        ProductDTO productUpdateDTO = new ProductDTO();
        for(Map.Entry<String,Object> key : productObj.entrySet()) {
            String keyName = key.getKey();
            if(keyName.equals("product") && Objects.nonNull(key.getValue())) {
                this.mappingProductInfo(key, productUpdateDTO);
            }

            if(keyName.equals("variant") && Objects.nonNull(key.getValue())) {
                this.mappingVariantInfo(key, productUpdateDTO);
            }

            if(keyName.equals("variant_images") && Objects.nonNull(key.getValue())) {
                this.mappingVariantImgInfo(key, productUpdateDTO);
            }

            if(keyName.equals("variant_attributes") && Objects.nonNull(key.getValue())) {
                this.mappingVariantAttributeInfo(key, productUpdateDTO);
            }
        }
        System.out.println(productUpdateDTO);

    }

    @org.springframework.transaction.annotation.Transactional
    public void updateProduct(ProductDTO productUpdateDTO, MultipartFile[] imageFile) throws ParseException {

        ProductEntity product = null;
        ProductCreateDTO productCreate = productUpdateDTO.getProduct();
        if(!ObjectUtils.isEmpty(productCreate)) {
            CategoryEntity category = categoryRepository.findById(
                            productCreate.getCategory()
                                    .getId())
                    .orElseThrow(() -> new ServiceException("CT-002", "ស្វែងរកមិនឃើញទេ! លេខរៀង: " + productCreate.getCategory().getId()));
            product = new ProductEntity();
            product.setCategory(category);
            product.setNameEn(productCreate.getNameEn());
            product.setNameKh(productCreate.getNameKh());
            product.setCode(productCreate.getCode());
            product.setDescription(productCreate.getDescription());
            product.setCurrency(productCreate.getCurrency());
            product.setSalePrice(productCreate.getSalePrice());
            product.setCreatedAt(DateTimeUtil.convertDate(new Date()));
            product.setStatus(Status.ACTIVE.getValue());
            product = productRepository.save(product);
        }
        assert product != null;

        VariantEntity variant = null;
        if(!ObjectUtils.isEmpty(productUpdateDTO.getVariant())) {
            variant = variantService.createVariant(productUpdateDTO.getVariant(), product.getId());
        }

        assert variant != null;
        if(!ObjectUtils.isEmpty(imageFile)) {
            imageService.uploadMultiFiles(imageFile, variant.getId());
        }

        if(!ObjectUtils.isEmpty(productUpdateDTO.getVariantAttribute())) {
            for (VariantAttributeDTO variantAttributeDTO : productUpdateDTO.getVariantAttribute()) {
                variantAttributeService.createVariantAttribute2(variantAttributeDTO, variant.getId());
            }
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void createProduct(String productJson, MultipartFile[] images) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> productObj = objectMapper.readValue(productJson, Map.class);

        ProductDTO newProductInformation = new ProductDTO();
        for(Map.Entry<String,Object> key : productObj.entrySet()) {

            String keyName = key.getKey();
            if(keyName.equals("product") && Objects.nonNull(key.getValue())) {
                this.mappingProductInfo(key, newProductInformation);
            }

            if(keyName.equals("variant") && Objects.nonNull(key.getValue())) {
                this.mappingVariantInfo(key, newProductInformation);
            }

            if(keyName.equals("variant_images") && Objects.nonNull(key.getValue())) {
                this.mappingVariantImgInfo(key, newProductInformation);
            }

            if(keyName.equals("variant_attributes") && Objects.nonNull(key.getValue())) {
                this.mappingVariantAttributeInfo(key, newProductInformation);
            }
        }
        this.createProduct(newProductInformation, images);
    }

    @org.springframework.transaction.annotation.Transactional
    public void createProduct(ProductDTO productCreateDTO, MultipartFile[] imageFile) throws ParseException {

        ProductEntity product = null;
        ProductCreateDTO productCreate = productCreateDTO.getProduct();
        if(!ObjectUtils.isEmpty(productCreate)) {
            CategoryEntity category = categoryRepository.findById(
                    productCreate.getCategory()
                            .getId())
                    .orElseThrow(() -> new ServiceException("CT-002", "ស្វែងរកមិនឃើញទេ! លេខរៀង: " + productCreate.getCategory().getId()));
            product = new ProductEntity();
            product.setCategory(category);
            product.setNameEn(productCreate.getNameEn());
            product.setNameKh(productCreate.getNameKh());
            product.setCode(productCreate.getCode());
            product.setDescription(productCreate.getDescription());
            product.setCurrency(productCreate.getCurrency());
            product.setSalePrice(productCreate.getSalePrice());
            product.setCreatedAt(DateTimeUtil.convertDate(new Date()));
            product.setStatus(Status.ACTIVE.getValue());
            product = productRepository.save(product);
        }
        assert product != null;

        VariantEntity variant = null;
        if(!ObjectUtils.isEmpty(productCreateDTO.getVariant())) {
            variant = variantService.createVariant(productCreateDTO.getVariant(), product.getId());
        }

        assert variant != null;
        if(!ObjectUtils.isEmpty(imageFile)) {
            imageService.uploadMultiFiles(imageFile, variant.getId());
        }

        if(!ObjectUtils.isEmpty(productCreateDTO.getVariantAttribute())) {
            for (VariantAttributeDTO variantAttributeDTO : productCreateDTO.getVariantAttribute()) {
                variantAttributeService.createVariantAttribute2(variantAttributeDTO, variant.getId());
            }
        }
    }

    public void delete(ID req) {
        productRepository.deleteById(req.getId());
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<ProductListDetailDTO> getProductDetail(Pageable pageable) {
        List<ProductListDetailDTO> getActiveProducts = this.getActiveProducts(pageable);

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


    public List<ProductListDetailDTO> getActiveProducts(Pageable pageable) {
        Page<Object[]> results = productRepository.findActiveProductsRaw(pageable);
        List<ProductListDetailDTO> products = new ArrayList<>();

        /** 0 -> 7 = Product*/
        /** 8 -> 11 = Category*/
        /** 12 -> 16 = Variants*/
        /** 17  = AttributeName(AttributeNameKh) : VariantAttributeValue*/
        /** 18  = ImageId:UUID*/

        for (Object[] row : results) {
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
            ProductListDetailDTO existingProduct = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (existingProduct == null) {
                existingProduct = new ProductListDetailDTO(
                        productId, nameEn, nameKh, code, salePrice, currency, description, status, createdAt, category, new ArrayList<>()
                );
                products.add(existingProduct);
            }
            if(variant != null) {
                existingProduct.getVariants().add(variant);
            }
        }
        return products;
    }

    void mappingProductInfo(Map.Entry<String,Object> key, ProductDTO productDTO) {
        Map<String,Object> product = (Map<String, Object>) key.getValue();
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setId(Integer.valueOf(String.valueOf(product.getOrDefault("id", 0))));
        productCreateDTO.setNameEn(String.valueOf(product.getOrDefault("name_en", null)));
        productCreateDTO.setNameKh(String.valueOf(product.getOrDefault("name_kh", null)));
        productCreateDTO.setCode(String.valueOf(product.getOrDefault("code", null)));
        Double salePriceInteger = Double.valueOf(String.valueOf(product.getOrDefault("sale_price", 0.0)));
        Double salePrice = salePriceInteger != null ? salePriceInteger.doubleValue() : 0.0;
        productCreateDTO.setSalePrice(salePrice);
        productCreateDTO.setCurrency(String.valueOf(product.getOrDefault("currency", null)));
        productCreateDTO.setDescription(String.valueOf(product.getOrDefault("description", null)));
        Map<String,Object> category = (Map<String, Object>) product.get("category");
        CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();

        String categoryIdString = (String) category.getOrDefault("id", "0");

        Integer categoryId = Integer.parseInt(categoryIdString);
        categoryCreateDTO.setId(categoryId);
        productCreateDTO.setCategory(categoryCreateDTO);

        productDTO.setProduct(productCreateDTO);
    }

    void mappingVariantInfo(Map.Entry<String,Object> key, ProductDTO productDTO) {
        Map<String,Object> variant = (Map<String, Object>) key.getValue();
        VariantCreateDTO variantDTO = new VariantCreateDTO();
        Double basePriceInteger = Double.valueOf((String) variant.getOrDefault("base_price", 0.0));
        Double basePrice = basePriceInteger != null ? basePriceInteger.doubleValue() : 0.0;
        variantDTO.setBasePrice(basePrice);
        variantDTO.setCurrency((String) variant.getOrDefault("currency", null));
        variantDTO.setStockQuantity((Integer) variant.getOrDefault("stock_quantity", 0));
        variantDTO.setSku((String) variant.getOrDefault("sku", null));
        productDTO.setVariant(variantDTO);
    }

    void mappingVariantImgInfo(Map.Entry<String,Object> key, ProductDTO productDTO) {
        List<VariantImageCreateDTO> listVariantImgDTO = new ArrayList<>();
        ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
        for(int i =0; i< arrayList.size();i++) {
            Map<String,Object> map = (Map<String, Object>) arrayList.get(i);
            VariantImageCreateDTO variantImageCreateDTO = new VariantImageCreateDTO();
            variantImageCreateDTO.setVariantId((Integer) map.getOrDefault("variant_id", null));
            listVariantImgDTO.add(variantImageCreateDTO);
        }
        productDTO.setVariantImage(listVariantImgDTO);
    }

    void mappingVariantAttributeInfo(Map.Entry<String,Object> key, ProductDTO productDTO) {
        List<VariantAttributeDTO> listVariantAttributeDTO = new ArrayList<>();
        ArrayList<?> arrayList = (ArrayList<?>) key.getValue();
        for(int i =0 ;i < arrayList.size();i++) {
            Map<String,Object> map = (Map<String, Object>) arrayList.get(i);
            VariantAttributeDTO variantAttributeDTO = new VariantAttributeDTO();
            variantAttributeDTO.setAttributeId((Integer) map.getOrDefault("attribute_id", null));
            variantAttributeDTO.setValue((String) map.getOrDefault("value", null));
            listVariantAttributeDTO.add(variantAttributeDTO);
        }
        productDTO.setVariantAttributes(listVariantAttributeDTO);
    }

}
