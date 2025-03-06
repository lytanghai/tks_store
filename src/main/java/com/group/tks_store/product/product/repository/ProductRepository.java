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

    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " + //7
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description,  " + //11
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity,  " + //16
            "STRING_AGG(DISTINCT a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " + //17
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " + //18
            "FROM product p " +
            "LEFT JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "LEFT JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "GROUP BY p.id, c.id, v.id ", nativeQuery = true)
    Page<Object[]> findActiveProductsRaw(Pageable pageable);

    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description,  " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity,  " +
            "STRING_AGG(DISTINCT a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "LEFT JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "LEFT JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND (:code IS NULL OR p.code = :code) " +
            "AND (:productName IS NULL OR p.name_en LIKE %:productName% OR p.name_kh LIKE %:productName%) " +
            "AND (:categoryName IS NULL OR c.name LIKE %:categoryName%) " +
            "AND (:salePrice IS NULL OR p.sale_price = :salePrice) " +
            "AND (:stockQty IS NULL OR v.stock_quantity = :stockQty) " +
            "AND (:sku IS NULL OR v.sku LIKE %:sku%) " +
            "GROUP BY p.id, c.id, v.id", nativeQuery = true)
    List<Object> getProductFilterDetailWithoutPagination2(
            @Param("code") String code,
            @Param("productName") String productName,
            @Param("categoryName") String categoryName,
            @Param("salePrice") Double salePrice,
            @Param("stockQty") Integer stockQty,
            @Param("sku") String sku
    );

    @Query(value = "SELECT * FROM product p INNER JOIN category c ON " +
            "p.category_id = c.id " +
            "WHERE (:code = '' OR p.code = :code) " +
            "AND ((:productName = '' OR p.name_en ILIKE %:productName%) " +
            "OR (:productName = '' OR p.name_kh ILIKE %:productName%)) " +
            "AND (:categoryName = '' OR c.name ILIKE %:categoryName% " +
            "OR c.name_kh ILIKE %:categoryName%) " +
            "AND (:salePrice IS NULL OR p.sale_price = :salePrice)", nativeQuery = true)
    List<ProductEntity> fetchProductByProperty(
            @Param("code") String code,
            @Param("productName") String productName,
            @Param("categoryName") String categoryName,
            @Param("salePrice") Double salePrice
    );
}