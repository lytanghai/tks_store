package com.group.tks_store.product.attribute.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.product.attribute.dto.AttributeCreateDTO;
import com.group.tks_store.product.attribute.dto.AttributeListDTO;
import com.group.tks_store.product.attribute.dto.AttributeUpdateDto;
import com.group.tks_store.product.attribute.entity.AttributeEntity;
import com.group.tks_store.product.attribute.repository.AttributeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

@Service
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttributeService {

    private final Logger log = LoggerFactory.getLogger(AttributeService.class);

    @Autowired
    private AttributeRepository attributeRepository;

    public List<AttributeEntity> getAllCategories() {
        return attributeRepository.findAll();
    }

    public void create(AttributeCreateDTO payloadRequest) throws ParseException {
        AttributeEntity category = new AttributeEntity();
        category.setName(payloadRequest.getName());
        category.setNameKh(payloadRequest.getNameKh());
        category.setStatus(Status.ACTIVE.getValue());
        this.attributeRepository.save(category);
    }

    public void update(AttributeUpdateDto payloadRequest) throws ParseException {
        AttributeEntity existCategory = attributeRepository.getById(payloadRequest.getId());
        if(Objects.nonNull(existCategory)) {
            existCategory.setName(payloadRequest.getName());
            existCategory.setNameKh(payloadRequest.getNameKh());
            this.attributeRepository.save(existCategory);
        }
    }

    public void delete(ID req) {
        attributeRepository.deleteById(req.getId());
    }

    public List<AttributeEntity> list() {
        return attributeRepository.findAllByActive(Status.ACTIVE.getValue());
    }

    public AttributeListDTO findAllByPagination(String status, PageRequest pageRequest) {
        log.info(status);
        Page<AttributeEntity> result  = attributeRepository.findAllByPagination(status, pageRequest);
        AttributeListDTO response = new AttributeListDTO();
        response.setRecords(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalRecords(result.getSize());
        response.setFirst(result.isFirst());
        response.setLast(result.isLast());
        response.setPageNumber(result.getPageable().getPageNumber());

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
}
