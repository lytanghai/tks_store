package com.group.tks_store.product.attribute.controller;

import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.attribute.dto.AttributeDTO;
import com.group.tks_store.product.attribute.dto.AttributeListDTO;
import com.group.tks_store.product.attribute.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(AddressRedirect.INTERNAL + AddressRedirect.ATTRIBUTE)
public class AttributeRestController {

    @Autowired
    private AttributeService attributeService;

    @GetMapping(CommonKey.LIST)
    public List<AttributeDTO> list() {
        return attributeService.list();
    }

    @GetMapping(CommonKey.LIST + "/filter")
    public ResponseEntity<Map<String, Object>> listFilter(@RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(name = CommonKey.SIZE, defaultValue = "10") Integer pageSize,
                                                          @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                          Model model) {
        AttributeListDTO result;
        if(!keyword.equals("")) {
            result = attributeService.findByKeyword(
                    keyword,
                    PageRequest.of(pageNumber,
                            pageSize,
                            Sort.Direction.DESC,
                            CommonKey.ID)
            );
        } else {
            result = attributeService.findAllByPagination(
                    Status.ACTIVE.getValue(),
                    PageRequest.of(pageNumber,
                            pageSize,
                            Sort.Direction.DESC,
                            CommonKey.ID)
            );
        }

        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", result.getPageNumber());
        response.put("totalItems", result.getRecords());
        response.put("totalPages", result.getTotalPages());

        return ResponseEntity.ok(response);
    }


}
