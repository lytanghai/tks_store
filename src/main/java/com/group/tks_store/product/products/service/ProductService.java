package com.group.tks_store.product.products.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import com.group.tks_store.product.products.dto.ProductCreateDTO;
import com.group.tks_store.product.products.dto.ProductListDTO;
import com.group.tks_store.product.products.dto.ProductUpdateDto;
import com.group.tks_store.product.products.entity.ProductEntity;
import com.group.tks_store.product.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public void create(ProductCreateDTO productCreateDTO) throws ParseException {

        CategoryEntity category = categoryRepository.findById(productCreateDTO.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category Id is not found"));

        ProductEntity product = new ProductEntity();
        product.setCategory(category);
        product.setNameEn(productCreateDTO.getNameEn());
        product.setNameKh(productCreateDTO.getNameKh());
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
            BigDecimal salePrice = (BigDecimal) result[3];
            String currency = (String) result[4];
            String description = (String) result[5];
            String status = (String) result[6];
            Date createdAt = (Date) result[7];
            Date lastUpdatedAt = (Date) result[8];
            String categoryName = (String) result[9];
            String categoryNameKh = (String) result[10];

            Double salePriceDouble = salePrice != null ? salePrice.doubleValue() : null;

            return new ProductListDTO(id, nameEn, nameKh, salePriceDouble, currency, description, status, createdAt, lastUpdatedAt, categoryName, categoryNameKh);
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
