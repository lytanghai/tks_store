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
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " + //17
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
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND (:code IS NULL OR :code = '' OR p.code ILIKE CONCAT('%', :code, '%')) " +
            "AND (:productName IS NULL OR :productName = '' OR p.name_en ILIKE CONCAT('%', :productName, '%') " +
            "OR p.name_kh ILIKE CONCAT('%', :productName, '%')) " +
            "AND (:categoryName IS NULL OR :categoryName = '' OR c.name ILIKE CONCAT('%', :categoryName, '%') " +
            "OR c.name_kh ILIKE CONCAT('%', :categoryName, '%')) " +
            "AND (:salePriceUSD IS NULL OR CAST(p.sale_price AS TEXT) ILIKE CONCAT('%', :salePriceUSD, '%')) " +
            "OR (:salePriceKHR IS NULL OR CAST(p.sale_price AS TEXT) ILIKE CONCAT('%', :salePriceKHR, '%')) " +
            "AND (:salePriceCurrency = '' OR p.currency = :salePriceCurrency) " +
            "AND (:sku IS NULL OR :sku = '' OR v.sku ILIKE CONCAT('%', :sku, '%')) " +
            "AND (:stockQuantity IS NULL OR :stockQuantity = -1 OR CAST(v.stock_quantity AS TEXT) ILIKE CONCAT('%', :stockQuantity, '%')) " +
            "AND (:variantAttributeValue IS NULL OR va.value ILIKE CONCAT('%', :variantAttributeValue, '%')) " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity",
            nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingContains(Pageable pageable,
                                                       @Param("code") String code,
                                                       @Param("productName") String productName,
                                                       @Param("categoryName") String categoryName,
                                                       @Param("salePriceUSD") Double salePriceUSD,
                                                       @Param("salePriceKHR") Double salePriceKHR,
                                                       @Param("salePriceCurrency") String salePriceCurrency,
                                                       @Param("sku") String sku,
                                                       @Param("stockQuantity") Integer stockQuantity,
                                                       @Param("variantAttributeValue") String variantAttributeValue);


    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' ||  a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND (:code IS NULL OR :code = '' OR p.code = :code) " +
            "AND (:productName IS NULL OR :productName = '' OR p.name_en = :productName OR p.name_kh = :productName) " +
            "AND (:categoryId IS NULL OR :categoryId = -1 OR c.id = :categoryId) " +
            "AND (:categoryName IS NULL OR :categoryName = '' OR c.name = :categoryName OR c.name_kh = :categoryName) " +
            "AND (:salePriceKHR IS NULL OR p.sale_price = :salePriceKHR) OR (:salePriceUSD IS NULL OR p.sale_price = :salePriceUSD) " +
            "AND (:salePriceCurrency = '' OR p.currency = :salePriceCurrency) " +
            "AND (:sku IS NULL OR :sku = '' OR v.sku = :sku) " +
            "AND (:stockQuantity IS NULL OR :stockQuantity = -1 OR v.stock_quantity = :stockQuantity) " +
            "AND (:variantAttributeValue IS NULL OR va.value = :variantAttributeValue) " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity", nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingEqual(Pageable pageable,
                                                       @Param("code") String code,
                                                       @Param("productName") String productName,
                                                       @Param("categoryId") Integer categoryId,
                                                       @Param("categoryName") String categoryName,
                                                       @Param("salePriceUSD") Double salePriceUSD,
                                                       @Param("salePriceKHR") Double salePriceKHR,
                                                       @Param("salePriceCurrency") String salePriceCurrency,
                                                       @Param("sku") String sku,
                                                       @Param("stockQuantity") Integer stockQuantity,
                                                       @Param("variantAttributeValue") String variantAttributeValue);




    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND ( " +
            "    (:salePriceCurrency = 'USD' AND p.currency = 'USD' AND p.sale_price BETWEEN :salePriceVal1 AND :salePriceVal2) " +
            " OR (:salePriceCurrency = 'KHR' AND p.currency = 'KHR' AND p.sale_price BETWEEN :salePriceVal1 AND :salePriceVal2) " +
            ") " +
            "AND (:stockQtyVal1 IS NULL OR :stockQtyVal1 = -1 OR v.stock_quantity >= :stockQtyVal1) " +
            "AND (:stockQtyVal2 IS NULL OR :stockQtyVal2 = -1 OR v.stock_quantity <= :stockQtyVal2) " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity", nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingBetween(
            Pageable pageable,
            @Param("salePriceVal1") Double salePriceVal1,
            @Param("salePriceVal2") Double salePriceVal2,
            @Param("salePriceCurrency") String salePriceCurrency,
            @Param("stockQtyVal1") Integer stockQtyVal1,
            @Param("stockQtyVal2") Integer stockQtyVal2);

    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND (:general IS NULL OR :general = '' OR p.code ILIKE CONCAT('%', :general, '%')) " +
            "OR p.name_en ILIKE CONCAT('%', :general, '%') " +
            "OR p.name_kh ILIKE CONCAT('%', :general, '%') " +
            "OR c.name ILIKE CONCAT('%', :general, '%') " +
            "OR c.name_kh ILIKE CONCAT('%', :general, '%') " +
            "OR v.sku ILIKE CONCAT('%', :general, '%') " +
            "OR CAST(p.sale_price AS TEXT) ILIKE CONCAT('%', :general, '%') " +
            "OR CAST(v.stock_quantity AS TEXT) ILIKE CONCAT('%', :general, '%') " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity",
            nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingGeneral(Pageable pageable,
                                                      @Param("general") String general);

    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND (:general IS NULL OR :general = '' OR p.code ILIKE CONCAT('%', :general, '%')) " +
            "OR p.name_en ILIKE CONCAT('%', :general, '%') " +
            "OR p.name_kh ILIKE CONCAT('%', :general, '%') " +
            "OR c.name ILIKE CONCAT('%', :general, '%') " +
            "OR c.name_kh ILIKE CONCAT('%', :general, '%') " +
            "OR v.sku ILIKE CONCAT('%', :general, '%') " +
            "OR CAST(p.sale_price AS TEXT) ILIKE CONCAT('%', :general, '%') " +
            "OR CAST(p.sale_price / 4100 AS TEXT) ILIKE CONCAT('%', :general, '%') " +
            "OR CAST(p.sale_price * 4100 AS TEXT) ILIKE CONCAT('%', :general, '%') " +
            "OR CAST(v.stock_quantity AS TEXT) ILIKE CONCAT('%', :general, '%') " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity",
            nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingStoreGeneral(Pageable pageable,
                                                      @Param("general") String general);


    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND ( " +
            "  (:salePriceCurrency = 'USD' AND p.sale_price > :salePriceUSD) OR " + // Compare sale_price in USD
            "  (:salePriceCurrency = 'KHR' AND p.sale_price > :salePriceKHR) " + // Compare sale_price in KHR
            ") " +
            "AND (:salePriceCurrency IS NULL OR :salePriceCurrency = '' OR p.currency = :salePriceCurrency) " +
            "AND (:stockQty IS NULL OR :stockQty = -1 OR v.stock_quantity > :stockQty) " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity", nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingGreaterThan(
            Pageable pageable,
            @Param("salePriceUSD") Double salePriceUSD,
            @Param("salePriceKHR") Double salePriceKHR,
            @Param("salePriceCurrency") String salePriceCurrency,
            @Param("stockQty") Integer stockQty);



    @Query(value = "SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id AS category_id, c.name AS category_name, c.name_kh AS category_name_kh, c.description AS category_description, " +
            "v.id AS variant_id, v.sku, v.base_price, v.currency as base_price_currency, v.stock_quantity, " +
            "STRING_AGG(DISTINCT va.id || ':' || a.name || '(' || a.name_kh || ') ' || ':' || va.value, ',') AS attributes, " +
            "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT i.id || ':' || i.uuid), ',') AS images " +
            "FROM product p " +
            "INNER JOIN category c ON p.category_id = c.id AND c.status = 'ACTIVE' " +
            "INNER JOIN variants v ON v.product_id = p.id AND v.status = 'ACTIVE' " +
            "LEFT JOIN images i ON i.variant_id = v.id " +
            "LEFT JOIN variant_attributes va ON va.variant_id = v.id " +
            "LEFT JOIN attributes a ON va.attribute_id = a.id " +
            "WHERE p.status = 'ACTIVE' " +
            "AND ( " +
            "  (:salePriceCurrency = 'USD' AND p.sale_price < :salePriceUSD) OR " + // Compare sale_price in USD
            "  (:salePriceCurrency = 'KHR' AND p.sale_price < :salePriceKHR) " + // Compare sale_price in KHR
            ") " +
            "AND (:salePriceCurrency IS NULL OR :salePriceCurrency = '' OR p.currency = :salePriceCurrency) " +
            "AND (:stockQty IS NULL OR :stockQty = -1 OR v.stock_quantity < :stockQty) " +
            "GROUP BY p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, " +
            "c.id, c.name, c.name_kh, c.description, v.id, v.sku, v.base_price, v.currency, v.stock_quantity", nativeQuery = true)
    Page<Object[]> fetchProductByPropertyUsingLessThan(
            Pageable pageable,
            @Param("salePriceUSD") Double salePriceUSD,
            @Param("salePriceKHR") Double salePriceKHR,
            @Param("salePriceCurrency") String salePriceCurrency,
            @Param("stockQty") Integer stockQtyVal);
}