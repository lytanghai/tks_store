package com.group.tks_store.product.category.controller;

import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.category.dto.CategoryCreateDTO;
import com.group.tks_store.product.category.dto.CategoryListDTO;
import com.group.tks_store.product.category.dto.CategoryUpdateDto;
import com.group.tks_store.product.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;


@Controller
@RequestMapping(CommonKey.API_CONTEXT_PATH + AddressRedirect.CATEGORY)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(CommonKey.CREATE)
    public String create(@RequestBody CategoryCreateDTO category) throws ParseException {
        categoryService.create(category);
        return AddressRedirect.REDIRECT_CATEGORY;
    }

    @PostMapping(CommonKey.DELETE)
    public String delete(@RequestBody ID id) {
        categoryService.delete(id);
        return AddressRedirect.REDIRECT_CATEGORY;
    }

    @PostMapping(CommonKey.UPDATE)
    public String delete(@RequestBody CategoryUpdateDto categoryUpdateDto) throws ParseException {
        categoryService.update(categoryUpdateDto);
        return AddressRedirect.REDIRECT_CATEGORY;
    }

    @GetMapping(CommonKey.LIST)
    public String redirect2Category(Model model, @RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber) {
        CategoryListDTO result = categoryService.findAllByPagination(
                CommonKey.STATUS,
                PageRequest.of(pageNumber,
                        10,
                        Sort.Direction.DESC,
                        CommonKey.ID)
        );

        model.addAttribute(CommonKey.PAGE_TYPE_EN, AddressRedirect.CATEGORY);
        model.addAttribute(CommonKey.PAGE_TYPE_KH, AddressRedirect.CATEGORY_KH);
        model.addAttribute(CommonKey.CONTENT, result.getRecords());
        model.addAttribute(CommonKey.TOTAL_RECORDS, result.getRecords().size());
        model.addAttribute(CommonKey.TOTAL_PAGES, result.getTotalPages());
        model.addAttribute(CommonKey.CURRENT_PAGE, result.getPageNumber());
        model.addAttribute(CommonKey.SORT_BY, result.getSortBy());
        model.addAttribute(CommonKey.SORT_DIRECTION, result.getSortDirection());
        model.addAttribute(CommonKey.FIRST, result.getFirst());
        model.addAttribute(CommonKey.LAST, result.getLast());

        return AddressRedirect.HOME;
    }
}
