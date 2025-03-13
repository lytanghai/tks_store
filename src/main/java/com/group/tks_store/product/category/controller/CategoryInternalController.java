package com.group.tks_store.product.category.controller;

import com.group.tks_store.common.enumz.Status;
import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(AddressRedirect.INTERNAL + AddressRedirect.CATEGORY)
public class CategoryInternalController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(CommonKey.LIST)
    public List<CategoryEntity> list() {
        return categoryService.list();
    }

    @GetMapping(CommonKey.LIST + "/filter")
    public ResponseEntity<Map<String, Object>> listFilter(@RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(name = CommonKey.SIZE, defaultValue = "10") Integer pageSize,
                                                          @RequestParam(name = CommonKey.SORT, defaultValue = "id") String sortBy,
                                                          @RequestParam(name = CommonKey.DIRECTION, defaultValue = "DESC") String sortDirection,
                                                          @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                          Model model) {

        CategoryListDTO result;
        if(!keyword.equals("")) {
            result = categoryService.findByKeyword(
                    keyword,
                    PageRequest.of(pageNumber,
                            pageSize,
                            Sort.Direction.DESC,
                            CommonKey.ID)
            );
        } else {
            result = categoryService.findAllByPagination(
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
