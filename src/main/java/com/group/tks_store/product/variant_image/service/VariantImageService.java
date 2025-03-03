package com.group.tks_store.product.variant_image.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.util.DateTimeUtil;
import com.group.tks_store.common.util.ImageUtil;
import com.group.tks_store.exception.ServiceException;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant_image.dto.VariantImageListDTO;
import com.group.tks_store.product.variant_image.entity.VariantImageEntity;
import com.group.tks_store.product.variant_image.repository.ProductVariantImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VariantImageService {

    @Autowired
    private ProductVariantImageRepository productVariantImageRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    public void create(Integer variantId, MultipartFile file) throws ParseException, IOException {

        VariantEntity variant = variantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Variant Id is not found"));

        VariantImageEntity variantImageEntity = new VariantImageEntity();
        variantImageEntity.setVariant(variant);
        if(Objects.nonNull(file)) {
            variantImageEntity.setImage(ImageUtil.convertImageToBase64(file));
        }

        variantImageEntity.setCreatedAt(DateTimeUtil.convertDate(new Date()));
        productVariantImageRepository.save(variantImageEntity);
    }

    public void update(Integer variantImageId, Integer variantId, MultipartFile file) throws ParseException, IOException {
        VariantImageEntity existVariantImage = productVariantImageRepository.getById(variantImageId);
        if(Objects.nonNull(existVariantImage)) {
            existVariantImage.setImage(ImageUtil.convertImageToBase64(file));
            VariantEntity existVariantEntity = variantRepository.findById(variantId).orElseThrow(() -> new ServiceException("VR-002","ស្វែងរកមិនឃើញទេ"));
            if(Objects.nonNull(existVariantEntity)) {
                existVariantImage.setVariant(existVariantEntity);
            }

            existVariantImage.setLastUpdatedAt(DateTimeUtil.convertDate(new Date()));
            this.productVariantImageRepository.save(existVariantImage);
        }
    }

    public void delete(ID req) {
        productVariantImageRepository.deleteById(req.getId());
    }

    public List<VariantImageEntity> getAllVariantImages() {
        return productVariantImageRepository.findAll();
    }

    public VariantImageEntity getVariantImageById(Integer id) {
        return productVariantImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant Image not found"));
    }

    public Page<VariantImageListDTO> getActiveVariantImage(Pageable pageable) {
        return productVariantImageRepository.fetchAllRecords(pageable).map(result -> {
            Integer id = (Integer) result[0];
            Integer variantId = (Integer) result[1];
            String image = (String) result[2];
            Date createdAt = (Date) result[3];
            Date lastUpdatedAt = (Date) result[4];

            return new VariantImageListDTO(id, variantId,image, createdAt, lastUpdatedAt);
        });
    }

    @Transactional
    public VariantImageEntity createVariantImage(VariantImageEntity variantImage) {
        if (variantImage.getVariant() != null && variantImage.getVariant().getId() != null) {
            VariantEntity category = variantRepository.findById(variantImage.getVariant().getId())
                    .orElseThrow(() -> new RuntimeException("Variant not found"));
            variantImage.setVariant(category);
        }
        return productVariantImageRepository.save(variantImage);
    }

    @Transactional
    public void deleteVariantImage(Integer id) {
        productVariantImageRepository.deleteById(id);
    }
}
