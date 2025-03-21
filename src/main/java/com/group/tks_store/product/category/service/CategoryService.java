package com.group.tks_store.product.category.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.exception.ServiceException;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void create(CategoryCreateDTO payloadRequest) throws ParseException {

        if(categoryRepository.findByName(payloadRequest.getName()) > 0) {
            throw new ServiceException("CT-001", "ឈ្មោះ " + payloadRequest.getName() + " មានរួចរាល់ហើយ!");
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(payloadRequest.getName());
        category.setNameKh(payloadRequest.getNameKh());
        category.setDescription(payloadRequest.getDescription().isEmpty() ? null : payloadRequest.getDescription());
        category.setStatus(Status.ACTIVE.getValue());
        category.setCreatedAt(DateTimeUtil.convertDate(new Date()));
        this.categoryRepository.save(category);
    }

    public void update(CategoryUpdateDto payloadRequest) throws ParseException {
        CategoryEntity existCategory = categoryRepository.getById(payloadRequest.getId());
        if(Objects.nonNull(existCategory)) {
            existCategory.setName(payloadRequest.getName());
            existCategory.setNameKh(payloadRequest.getNameKh());
            existCategory.setDescription(payloadRequest.getDescription());
            existCategory.setLastUpdatedAt(DateTimeUtil.convertDate(new Date()));
            this.categoryRepository.save(existCategory);
        } else {
            throw new ServiceException("CT-002", "ស្វែងរកមិនឃើញទេ! លេខរៀង: " + payloadRequest.getId());
        }
    }

    public void delete(ID req) {
        categoryRepository.deleteById(req.getId());
    }

    public List<CategoryEntity> list() {
        return categoryRepository.findAllByActive(Status.ACTIVE.getValue());
    }

    public CategoryListDTO findAllByPagination(String status, PageRequest pageRequest) {
        Page<CategoryEntity> result  = categoryRepository.findAllByPagination(status, pageRequest);
        CategoryListDTO response = new CategoryListDTO();
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
    public CategoryListDTO findByKeyword(String keyword, PageRequest pageRequest) {
        Page<CategoryEntity> result  = categoryRepository.findByKeyword(keyword, pageRequest);
        CategoryListDTO response = new CategoryListDTO();
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
