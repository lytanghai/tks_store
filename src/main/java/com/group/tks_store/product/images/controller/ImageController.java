package com.group.tks_store.product.images.controller;

import com.group.tks_store.common.util.FileNameUtil;
import com.group.tks_store.product.images.dto.ImageResponseDTO;
import com.group.tks_store.product.images.entity.Image;
import com.group.tks_store.product.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class ImageController {

	@Autowired
	private ImageService imageService;

	private FileNameUtil fileHelper = new FileNameUtil();

	@GetMapping("/get/images")
	public ResponseEntity<List<String>> getImageUUIDs(@RequestParam("variant_id") Integer variantId) {
		List<String> imageUUIDs = new ArrayList<>();
		try {
			if (variantId != null) {
				List<Image> images = imageService.findByVariantId(variantId);
				for (Image img : images) {
					imageUUIDs.add(img.getUuid());  // Store UUID instead of binary
				}
			}
			return ResponseEntity.ok(imageUUIDs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/image/show")
	public ResponseEntity<byte[]> getImageByUUID(@RequestParam("uuid") String uuid) {
		try {
			Image image = imageService.findByUuid(uuid);
			if (image == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);  // Change based on file type

			return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/images")
	public ResponseEntity<List<ImageResponseDTO>> getAllImageInfo() throws Exception {
		List<ImageResponseDTO> imageResponses = imageService.findAllImageResponse();
		return ResponseEntity.ok().body(imageResponses);
	}

	@PostMapping("/upload")
	public ImageResponseDTO uploadSingleFile(@RequestParam("file") MultipartFile file) {
		Image image = Image.buildImage(file, fileHelper);
		imageService.save(image);
		return new ImageResponseDTO(image);
	}

	@PostMapping("/uploads")
	public List<ImageResponseDTO> uploadMultiFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadSingleFile(file)).collect(Collectors.toList());
	}

	@GetMapping("/show/{fileName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws Exception {
		Image image = getImageByName(fileName);
		return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
	}

	@GetMapping("/show")
	public ResponseEntity<byte[]> getImageWithRequestParam(
			@RequestParam(required = false, value = "uuid") String uuid,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "id") Integer id) throws Exception {

		if(id != null) {
			Image image = imageService.getImageById(id);
			return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
		}
		else if (uuid != null) {
			Image image = getImageByUuid(uuid);
			return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
		} else if (name != null) {
			return getImage(name);
		}
		Image defaultImage = Image.defaultImage();
		return ResponseEntity.ok().contentType(MediaType.valueOf(defaultImage.getFileType()))
				.body(defaultImage.getData());

	}

	@GetMapping("/list")
	public ResponseEntity<byte[]> getImagesWithUUIDList(@RequestParam(value = "variant_id") Integer variantId) {
		List<byte[]> imageList = new ArrayList<>();

		try {
			if (variantId != null) {
				List<Image> image = imageService.findByVariantId(variantId);
				for(int i=0;i<image.size();i++) {
					imageList.add(image.get(i).getData());
				}
			}
			int totalLength = imageList.stream().mapToInt(arr -> arr.length).sum();
			byte[] combinedData = new byte[totalLength];
			int destPos = 0;
			for (byte[] data : imageList) {
				System.arraycopy(data, 0, combinedData, destPos, data.length);
				destPos += data.length;
			}

			// Set the content type based on the file type of the images
			MediaType mediaType = MediaType.IMAGE_JPEG; // Adjust based on actual file type

			// Return the combined byte array with appropriate content type
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			return new ResponseEntity<>(combinedData, headers, HttpStatus.OK);

		} catch (Exception e) {
			// Handle exception appropriately
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/show/{width}/{height}")
	public ResponseEntity<byte[]> getScaledImageWithRequestParam(@PathVariable int width, @PathVariable int height,
			@RequestParam(required = false, value = "uuid") String uuid,
			@RequestParam(required = false, value = "name") String name) throws Exception {

		if (uuid != null) {
			Image image = getImageByUuid(uuid, width, height);
			return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
		}
		if (name != null) {
			Image image = getImageByName(name, width, height);
			return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
		}
		Image defImage = Image.defaultImage(width, height);
		return ResponseEntity.ok().contentType(MediaType.valueOf(defImage.getFileType())).body(defImage.getData());
	}

	@GetMapping("/show/{width}/{height}/{fileName:.+}")
	public ResponseEntity<byte[]> getScaledImage(@PathVariable int width, @PathVariable int height,
			@PathVariable String fileName) throws Exception {
		Image image = getImageByName(fileName, width, height);
		return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
	}

	public Image getImageByName(String name) throws Exception {
		Image image = imageService.findByFileName(name);
		if (image == null) {
			return Image.defaultImage();
		}
		return image;
	}

	public Image getImageByName(String name, int width, int height) throws Exception {
		Image image = imageService.findByFileName(name);
		if (image == null) {
			Image defImage = Image.defaultImage();
			defImage.scale(width, height);
			return defImage;
		}
		image.scale(width, height);
		return image;
	}

	public Image getImageByUuid(String uuid) throws Exception {
		Image image = imageService.findByUuid(uuid);
		if (image == null) {
			return Image.defaultImage();
		}
		return image;
	}

	public Image getImageByUuid(String uuid, int width, int height) throws Exception {
		Image image = imageService.findByUuid(uuid);
		if (image == null) {
			Image defImage = Image.defaultImage();
			defImage.scale(width, height);
			return defImage;
		}
		image.scale(width, height);
		return image;
	}

}
