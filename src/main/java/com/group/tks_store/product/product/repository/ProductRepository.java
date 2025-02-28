package com.group.tks_store.product.product.repository;

import com.group.tks_store.product.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query(value = " SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, p.last_updated_at, c.name AS categoryName, c.name_kh AS categoryNameKh FROM product p INNER JOIN category c ON p.category_id = c.id WHERE p.status = :status ", nativeQuery = true)
    Page<Object[]> findByStatus(@Param("status") String status, Pageable pageable);

//    @Query(value = " SELECT p.id, p.name_en, p.name_kh, p.code, p.sale_price, p.currency, p.description, p.status, p.created_at, p.last_updated_at, c.name AS categoryName, c.name_kh AS categoryNameKh FROM product p " +
//            "INNER JOIN category c ON p.category_id = c.id WHERE p.status = :status ", nativeQuery = true)
//    Page<Object[]> findByStatusV2(@Param("status") String status, Pageable pageable);

}