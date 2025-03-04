package com.group.tks_store.product.variant_attribute.service;

import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.exception.ServiceException;
import com.group.tks_store.product.attribute.entity.AttributeEntity;
import com.group.tks_store.product.attribute.repository.AttributeRepository;
import com.group.tks_store.product.variant.entity.VariantEntity;
import com.group.tks_store.product.variant.repository.ProductVariantRepository;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDTO;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeDetailList;
import com.group.tks_store.product.variant_attribute.dto.VariantAttributeListDTO;
import com.group.tks_store.product.variant_attribute.dto.interfaze.VariantAttributeDetailInterface;
import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;
import com.group.tks_store.product.variant_attribute.repository.VariantAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VariantAttributeService {

    @Autowired
    private VariantAttributeRepository variantAttributeRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    public VariantAttributeDTO createVariantAttribute(VariantAttributeDTO dto) {
        VariantEntity variant = variantRepository.findById(dto.getVariantId())
                .orElseThrow(() -> new ServiceException("VR-002","រកវ៉ារ្យ៉ង់មិនឃើញទេ"));
        
        AttributeEntity attribute = attributeRepository.findById(dto.getAttributeId())
                .orElseThrow(() -> new ServiceException("AT-002","រកអង្គធាតុមិនឃើញទេ"));

        VariantAttributeEntity entity = new VariantAttributeEntity();
        entity.setVariant(variant);
        entity.setAttribute(attribute);
        entity.setValue(dto.getValue());
        entity.setStatus(dto.getStatus());

        entity = variantAttributeRepository.save(entity);

        return new VariantAttributeDTO(entity.getId(), dto.getVariantId(), dto.getAttributeId(), entity.getValue(), entity.getStatus());
    }

    public VariantAttributeEntity createVariantAttribute2(VariantAttributeDTO dto, Integer variantId) {
        VariantEntity variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ServiceException("VR-002","រកវ៉ារ្យ៉ង់មិនឃើញទេ"));

        AttributeEntity attribute = attributeRepository.findById(dto.getAttributeId())
                .orElseThrow(() -> new ServiceException("AT-002","រកអង្គធាតុមិនឃើញទេ"));

        VariantAttributeEntity entity = new VariantAttributeEntity();
        entity.setVariant(variant);
        entity.setAttribute(attribute);
        entity.setValue(dto.getValue());
        entity.setStatus(dto.getStatus());

        entity = variantAttributeRepository.save(entity);

        return entity;
    }

    // Get Variant Attribute by ID
    public VariantAttributeDTO getVariantAttributeById(Integer id) {
        VariantAttributeEntity entity = variantAttributeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("VA-002","រកវ៉ារ្យ៉ង់ និងអង្គធាតុមិនឃើញទេ"));
        return new VariantAttributeDTO(entity.getId(), entity.getVariant().getId(), entity.getAttribute().getId(), entity.getValue(), entity.getStatus());
    }

    // Get all Variant Attributes
    public List<VariantAttributeDTO> getAllVariantAttributes() {
        return variantAttributeRepository.findAll()
                .stream()
                .map(entity -> new VariantAttributeDTO(entity.getId(), entity.getVariant().getId(), entity.getAttribute().getId(), entity.getValue(), entity.getStatus()))
                .collect(Collectors.toList());
    }

    // Update Variant Attribute
    public VariantAttributeDTO updateVariantAttribute(Integer id, VariantAttributeDTO dto) {
        VariantAttributeEntity entity = variantAttributeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("VA-002","រកវ៉ារ្យ៉ង់ និងអង្គធាតុមិនឃើញទេ"));

        VariantEntity variant = variantRepository.findById(dto.getVariantId())
                .orElseThrow(() -> new ServiceException("VR-002","រកវ៉ារ្យ៉ង់មិនឃើញទេ"));

        AttributeEntity attribute = attributeRepository.findById(dto.getAttributeId())
                .orElseThrow(() ->  new ServiceException("AT-002","រកអង្គធាតុមិនឃើញទេ"));

        entity.setVariant(variant);
        entity.setAttribute(attribute);
        entity.setValue(dto.getValue());
        entity.setStatus(dto.getStatus());

        entity = variantAttributeRepository.save(entity);

        return new VariantAttributeDTO(entity.getId(), entity.getVariant().getId(), entity.getAttribute().getId(), entity.getValue(), entity.getStatus());
    }

    // Delete Variant Attribute
    public void deleteVariantAttribute(Integer id) {
        if (!variantAttributeRepository.existsById(id)) {
            throw new ServiceException("VA-002","រកវ៉ារ្យ៉ង់ និងអង្គធាតុមិនឃើញទេ");
        }
        variantAttributeRepository.deleteById(id);
    }

    public VariantAttributeListDTO findAllByPagination(String status, PageRequest pageRequest) {
        Page<VariantAttributeEntity> result = variantAttributeRepository.findByStatus(status, pageRequest);
        VariantAttributeListDTO response = new VariantAttributeListDTO();

        response.setRecords(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalRecords(result.getTotalElements());
        response.setFirst(result.isFirst());
        response.setLast(result.isLast());
        response.setPageNumber(result.getNumber());

        if (pageRequest.getSort().isSorted()) {
            pageRequest.getSort().get().findFirst().ifPresent(order -> {
                response.setSortBy(order.getProperty());
                response.setSortDirection(order.getDirection().toString());
            });
        } else {
            response.setSortBy(null);
            response.setSortDirection(null);
        }

        return response;
    }

    public List<VariantAttributeDetailList> findByVariantId(Integer variantId) {
        List<VariantAttributeDetailList> response = new ArrayList<>();
        List<VariantAttributeDetailInterface> result = variantAttributeRepository.findVariantAttributeDetailByVariantId(variantId);
        if(!result.isEmpty()) {
            for (VariantAttributeDetailInterface variantAttr: result) {
                VariantAttributeDetailList each = new VariantAttributeDetailList();
                each.setVariantAttributeId(variantAttr.getVariantAttributeId());
                each.setValue(variantAttr.getValue());
                if(!ObjectUtils.isEmpty(variantAttr.getAttributeName()) && !ObjectUtils.isEmpty(variantAttr.getAttributeNameKh())) {
                    each.setAttributeName(variantAttr.getAttributeName() + "(" + variantAttr.getAttributeNameKh() + ")");
                } else if(ObjectUtils.isEmpty(variantAttr.getAttributeName())) {
                    each.setAttributeName(variantAttr.getAttributeNameKh());
                } else if(ObjectUtils.isEmpty(variantAttr.getAttributeNameKh())) {
                    each.setAttributeName(variantAttr.getAttributeName());
                }
                each.setAttributeId(variantAttr.getAttributeId());
                each.setVariantId(variantId);
                response.add(each);
            }
        }
        return response;
    }
}