package com.group.tks_store.product.images.service.impl;

import com.group.tks_store.common.util.FileNameUtil;
import com.group.tks_store.product.images.dto.ImageResponseDTO;
import com.group.tks_store.product.images.entity.Image;
import com.group.tks_store.product.images.repository.ImageRepository;
import com.group.tks_store.product.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ImageRepository imageRepository;

	private FileNameUtil fileHelper = new FileNameUtil();

	@Override
	public Image save(Image image) throws NullPointerException {
		if (image == null)
			throw new NullPointerException("Image Data NULL");
		return imageRepository.save(image);
	}

	@Override
	public Image findByFileName(String fileName) {
		return this.imageRepository.findByFileName(fileName);
	}

	@Override
	public Image findByUuid(String uuid) {
		return this.imageRepository.findByUuid(uuid);
	}

	@Override
	public List<ImageResponseDTO> findAllImageResponse() {
		return this.imageRepository.findAllImageResponse();
	}

	@Override
	public ResponseEntity<List<ImageResponseDTO>> getAllImageInfo() {
		List<ImageResponseDTO> imageResponses = this.findAllImageResponse();
		return ResponseEntity.ok().body(imageResponses);
	}

	@Override
	public ImageResponseDTO uploadSingleFile(MultipartFile file, Integer variantId) {
		Image image = Image.buildImage(file, fileHelper);
		image.setVariantId(variantId);
		this.save(image);
		return new ImageResponseDTO(image);
	}

	public List<ImageResponseDTO> uploadMultiFiles(MultipartFile[] files, Integer variantId) {
		return Arrays.asList(files).stream().map(file -> uploadSingleFile(file, variantId)).collect(Collectors.toList());
	}

	@Override
	public List<Image> findByVariantId(Integer variantId) {
		return imageRepository.findByVariantId(variantId);
	}

	@Override
	public Image getImageById(Integer id) {
		return imageRepository.findById(id).orElse(null);
	}
}
