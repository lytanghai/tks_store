package com.group.tks_store.product.product.repository;

import com.group.tks_store.product.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query(value = " SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, p.last_updated_at, c.name AS categoryName, c.name_kh AS categoryNameKh FROM product p INNER JOIN category c ON p.category_id = c.id WHERE p.status = :status ", nativeQuery = true)
    Page<Object[]> findByStatus(@Param("status") String status, Pageable pageable);

    @Query(value = " SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status," +
            " c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description,  " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity,  " +
            "ARRAY_TO_STRING(ARRAY_AGG(vi.image), ',') AS images, ARRAY_TO_STRING(ARRAY_AGG(va.attribute_id || ':' || va.value), ',') " +
            "AS attributes FROM product p LEFT JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' LEFT JOIN variants v " +
            "ON v.product_id = p.id AND v.status = 'ACTIVE' LEFT JOIN variant_images vi ON vi.variant_id = v.id" +
            " LEFT JOIN variant_attributes va ON va.variant_id = v.id WHERE p.status = 'ACTIVE' GROUP BY p.id, c.id, v.id ", nativeQuery = true)
    List<Object[]> findActiveProductsRaw();



}