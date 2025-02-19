package com.group.tks_store.product.products.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.products.dto.ProductCreateDTO;
import com.group.tks_store.product.products.dto.ProductListDTO;
import com.group.tks_store.product.products.dto.ProductUpdateDto;
import com.group.tks_store.product.products.entity.ProductEntity;
import com.group.tks_store.product.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void create(ProductCreateDTO productCreateDTO) throws ParseException {
        ProductEntity product = new ProductEntity();
        product.setNameEn(productCreateDTO.getNameEn());
        product.setNameKh(productCreateDTO.getNameKh());
        product.setCategoryId(productCreateDTO.getCategoryId());
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
            existProduct.setCategoryId(payloadRequest.getCategoryId());
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

    public List<ProductEntity> list() {
        return productRepository.findAllByActive(Status.ACTIVE.getValue());
    }

    public ProductListDTO findAllByPagination(String status, PageRequest pageRequest) {
        Page<ProductEntity> result  = productRepository.findAllByPagination(status, pageRequest);
        ProductListDTO response = new ProductListDTO();
        response.setRecords(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalRecords(result.getSize());
        response.setFirst(result.isFirst());
        response.setLast(result.isLast());
        response.setPageNumber(result.getPageable().getPageNumber());

        if (pageRequest.getSort().isSorted()) {
            pageRequest.getSort().get().findFirst().ifPresent(order -> {
                response.setSortBy(order.getProperty());
                response.setSortDirection(order.getDirection().toString());
            });
        } else {
            response.setSortBy(null);
            response.setSortDirection(null);
        }

        return response;
    }
}
