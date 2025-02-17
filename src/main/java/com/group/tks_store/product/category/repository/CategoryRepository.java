package com.group.tks_store.product.category.repository;

import com.group.tks_store.product.category.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM categories where status = 'ACTIVE'")
    List<CategoryEntity> findAllByActive(String status);

    @Query(value = "SELECT * FROM categories " +
            "WHERE (status = :status) ",
            nativeQuery = true)
    Page<CategoryEntity> findAllByPagination(@Param("status") String status,
                                             Pageable pageable);
}
