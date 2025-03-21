package com.group.tks_store.product.variant.repository;

import com.group.tks_store.product.variant.entity.VariantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<VariantEntity, Integer> {

    @Query(value = "SELECT v.id, v.sku, v.base_price, v.currency, v.stock_quantity, v.status, v.created_at, v.last_updated_at, " +
            "p.name_en AS productName, p.name_kh AS productNameKh " +
            "FROM variants v " +
            "INNER JOIN product p ON p.id = v.product_id " +  // Fixed join condition
            "WHERE v.status = :status",  // Fixed filter (variants should have status)
            nativeQuery = true)
    Page<Object[]> findByStatus(@Param("status") String status, Pageable pageable);

    VariantEntity findByProductId(Integer id);
}
