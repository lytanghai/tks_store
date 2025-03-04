package com.group.tks_store.product.variant_attribute.repository;

import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDetailList;
import com.group.tks_store.product.variant_attribute.dto.interfaze.VariantAttributeDetailInterface;
import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttributeEntity, Integer> {
    List<VariantAttributeEntity> findByVariantId(Integer variantId);
    Page<VariantAttributeEntity> findByStatus(String status, PageRequest pageRequest);

    @Query(value = "SELECT va.id as variantAttributeId, v.id as variantId, a.id as attributeId, " +
            " a.name as attributeName, a.name_kh as attributeNameKh, va.value as value " +
            " FROM variant_attributes va " +
            " INNER JOIN variants v on v.id = va.variant_id " +
            " INNER JOIN attributes a ON a.id = va.attribute_id " +
            " WHERE va.variant_id = :variantId " +
            " AND a.status = 'ACTIVE' " +
            " AND v.status = 'ACTIVE' " +
            " AND va.status = 'ACTIVE'", nativeQuery = true)
    List<VariantAttributeDetailInterface> findVariantAttributeDetailByVariantId(@Param("variantId") Integer variantId);

}