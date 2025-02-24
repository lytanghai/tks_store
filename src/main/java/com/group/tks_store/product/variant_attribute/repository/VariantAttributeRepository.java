package com.group.tks_store.product.variant_attribute.repository;

import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttributeEntity, Integer> {
    List<VariantAttributeEntity> findByVariantId(Integer variantId);
    Page<VariantAttributeEntity> findByStatus(String status, PageRequest pageRequest);

}