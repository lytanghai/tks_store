package com.group.tks_store.product.variant_image.repository;

import com.group.tks_store.product.variant_image.entity.VariantImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantImageRepository extends JpaRepository<VariantImageEntity, Integer> {

    @Query(value = "SELECT vi.id, v.id as variant_id, vi.image, vi.created_at, vi.last_updated_at " +
            "FROM variant_images vi " +
            "INNER JOIN variants v ON v.id = vi.variant_id ",
            nativeQuery = true)
    Page<Object[]> fetchAllRecords(Pageable pageable);


}
