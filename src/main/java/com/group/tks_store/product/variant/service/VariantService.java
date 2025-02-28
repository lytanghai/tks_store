package com.group.tks_store.product.variant.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.dto.VariantCreateDTO;
import com.group.tks_store.product.variant.dto.VariantListDTO;
import com.group.tks_store.product.variant.dto.VariantUpdateDto;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VariantService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public void create(VariantCreateDTO variantCreateDTO) throws ParseException {

        ProductEntity product = productRepository.findById(variantCreateDTO.getProduct().getId()).orElseThrow(() -> new RuntimeException("Product is not found"));

        VariantEntity variant = new VariantEntity();
        variant.setProduct(product);

        variant.setBasePrice(variantCreateDTO.getBasePrice());
        variant.setSku(variantCreateDTO.getSku());
        variant.setCurrency(variantCreateDTO.getCurrency());
        variant.setStockQuantity(variantCreateDTO.getStockQuantity());
        variant.setStatus(Status.ACTIVE.getValue());
        variant.setCreatedAt(DateTimeUtil.convertDate(new Date()));
        productVariantRepository.save(variant);
    }

    public VariantEntity createVariant(VariantCreateDTO variantCreateDTO, Integer productId) throws ParseException {

        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product is not found"));

        VariantEntity variant = new VariantEntity();
        variant.setProduct(product);

        variant.setBasePrice(variantCreateDTO.getBasePrice());
        variant.setSku(variantCreateDTO.getSku());
        variant.setCurrency(variantCreateDTO.getCurrency());
        variant.setStockQuantity(variantCreateDTO.getStockQuantity());
        variant.setStatus(Status.ACTIVE.getValue());
        variant.setCreatedAt(DateTimeUtil.convertDate(new Date()));
        return productVariantRepository.save(variant);
    }

    public void update(VariantUpdateDto payloadRequest) throws ParseException {
        VariantEntity existProduct = productVariantRepository.getById(payloadRequest.getId());
        if(existProduct != null) {
            existProduct.setBasePrice(payloadRequest.getBasePrice());
            existProduct.setCurrency(payloadRequest.getCurrency());
            existProduct.setSku(payloadRequest.getSku());
            existProduct.setStockQuantity(payloadRequest.getStockQuantity());
            existProduct.setLastUpdatedAt(DateTimeUtil.convertDate(new Date()));
            this.productVariantRepository.save(existProduct);
        }
    }

    public void delete(ID req) {
        productVariantRepository.deleteById(req.getId());
    }

    public List<VariantEntity> getAllVariants() {
        return productVariantRepository.findAll();
    }

    public Page<VariantListDTO> getActiveVariants(Pageable pageable) {
        return productVariantRepository.findByStatus("ACTIVE", pageable).map(result -> {
            Integer id = (Integer) result[0];
            String sku = (String) result[1];
            BigDecimal basePrice = (BigDecimal) result[2];
            String currency = (String) result[3];
            Integer stockQuantity = (Integer) result[4];
            String status = (String) result[5];
            Date createdAt = (Date) result[6];
            Date lastUpdatedAt = (Date) result[7];
            String productName = (String) result[8];
            String productNameKh = (String) result[9];

            return new VariantListDTO(id, sku, basePrice, currency,stockQuantity, status,createdAt, lastUpdatedAt,
                    productName, productNameKh);
        });
    }

    @Transactional
    public VariantEntity createProduct(VariantEntity variant) {
        if (variant.getProduct() != null && variant.getProduct().getId() != null) {
            ProductEntity product = productRepository.findById(variant.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            variant.setProduct(product);
        }
        return productVariantRepository.save(variant);
    }
}
