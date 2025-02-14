package com.group.tks_store.product.category.service;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void create(CategoryCreateDTO payloadRequest) {
        CategoryEntity category = new CategoryEntity();
        category.setName(payloadRequest.getName());
        category.setNameKh(payloadRequest.getNameKh());
        category.setDescription(payloadRequest.getDescription().isEmpty() ? null : payloadRequest.getDescription());
        category.setStatus(Status.ACTIVE.getValue());
        category.setCreatedAt(new Date());
        this.categoryRepository.save(category);
    }

    public void update(CategoryUpdateDto payloadRequest) {
        CategoryEntity existCategory = categoryRepository.getById(payloadRequest.getId());
        if(Objects.nonNull(existCategory)) {
            existCategory.setName(payloadRequest.getName());
            existCategory.setNameKh(payloadRequest.getNameKh());
            existCategory.setDescription(payloadRequest.getDescription());
            existCategory.setLastUpdatedAt(new Date());
            this.categoryRepository.save(existCategory);
        }
    }

    public void delete(ID req) {
        categoryRepository.deleteById(req.getId());
    }

    public List<CategoryEntity> list() {
        return categoryRepository.findAllByActive(Status.ACTIVE.getValue());
    }
}
