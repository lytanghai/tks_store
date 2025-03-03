package com.group.tks_store.product.product.service;

import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import com.group.tks_store.product.images.repository.ImageRepository;
import com.group.tks_store.product.images.service.ImageService;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductUpdateDto;
import com.group.tks_store.product.product.entity.ProductEntity;
import com.group.tks_store.product.product.repository.ProductRepository;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant.service.VariantService;
import com.group.tks_store.product.variant_attribute.service.VariantAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

@Service
public class ProductServiceBK {

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
}
