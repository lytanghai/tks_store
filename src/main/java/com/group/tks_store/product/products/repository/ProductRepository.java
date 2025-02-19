package com.group.tks_store.product.products.repository;

import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.products.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM product where status = 'ACTIVE'")
    List<ProductEntity> findAllByActive(String status);

    @Query(value = "SELECT * FROM product " +
            "WHERE (status = :status) ",
            nativeQuery = true)
    Page<ProductEntity> findAllByPagination(@Param("status") String status,
                                             Pageable pageable);


}