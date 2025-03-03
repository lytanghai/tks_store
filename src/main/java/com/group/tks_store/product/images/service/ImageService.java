package com.group.tks_store.product.images.service;

import com.group.tks_store.product.images.dto.ImageResponseDTO;
import com.group.tks_store.product.images.entity.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

	Image save(Image image);

	Image findByFileName(String fileName);

	Image findByUuid(String uuid);

	List<Image> findByVariantId(Integer variantId);

	List<ImageResponseDTO> findAllImageResponse();

	ResponseEntity<List<ImageResponseDTO>> getAllImageInfo();

	ImageResponseDTO uploadSingleFile(MultipartFile file, Integer variantId);

	List<ImageResponseDTO> uploadMultiFiles(MultipartFile[] files, Integer variantId);

	Image getImageById(Integer id);
}
