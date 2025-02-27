package com.group.tks_store.product.attribute.repository;

import com.group.tks_store.product.attribute.dto.AttributeDTO;
import com.group.tks_store.product.attribute.entity.AttributeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM attributes where status = 'ACTIVE'")
    List<AttributeEntity> findAllByActive();

    @Query(value = "SELECT * FROM attributes " +
            "WHERE (status = :status) ",
            nativeQuery = true)
    Page<AttributeEntity> findAllByPagination(@Param("status") String status,
                                             Pageable pageable);
}
