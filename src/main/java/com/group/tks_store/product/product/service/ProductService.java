package com.group.tks_store.product.product.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.exception.ServiceException;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductFullCreateDTO;
import com.group.tks_store.product.product.dto.ProductListDTO;
import com.group.tks_store.product.product.dto.ProductUpdateDto;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant.service.VariantService;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;
import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public void createFullProduct(ProductFullCreateDTO productCreateDTO) throws ParseException {

        ProductEntity product = null;
        if(!ObjectUtils.isEmpty(productCreateDTO.getProduct())) {
            product = this.createProduct(productCreateDTO.getProduct());
        }

        VariantEntity variant = null;
        if(!ObjectUtils.isEmpty(productCreateDTO.getVariant())) {
            variant = variantService.createVariant(productCreateDTO.getVariant(), product.getId());
        }

        VariantAttributeEntity variantAttribute = null;
        if(!ObjectUtils.isEmpty(productCreateDTO.getVariantAttribute())) {
            for (VariantAttributeDTO variantAttributeDTO : productCreateDTO.getVariantAttribute()) {
                variantAttribute = variantAttributeService.createVariantAttribute2(variantAttributeDTO, variant.getId());
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

    @Transactional
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
