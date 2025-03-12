package com.group.tks_store.product.images.repository;

import com.group.tks_store.product.images.dto.ImageResponseDTO;
import com.group.tks_store.product.images.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

	Image findByFileName(String fileName);

	Image findByUuid(String uuid);

	@Query(value = "select new com.group.tks_store.product.images.dto.ImageResponseDTO(im.uuid, im.fileName, im.fileType, im.size) from com.group.tks_store.product.images.entity.Image im where im.status=true", nativeQuery = false)
	List<ImageResponseDTO> findAllImageResponse();

	List<Image> findByVariantId(Integer variantId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM images WHERE uuid IN (:uuid)" , nativeQuery = true)
    void deleteByUuid(List<String> uuid);
}