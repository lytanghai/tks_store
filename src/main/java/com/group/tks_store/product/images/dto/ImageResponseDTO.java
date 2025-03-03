package com.group.tks_store.product.images.dto;

import com.group.tks_store.product.images.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDTO {
	private String uuid;
	private String fileName;
	private String fileType;
	private long size;

	public ImageResponseDTO(Image image) {
		setUuid(image.getUuid());
		setFileName(image.getFileName());
		setFileType(image.getFileType());
		setSize(image.getSize());
	}
}