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

    @PostMapping("/create")
    public String create(@RequestBody CategoryCreateDTO category) throws ParseException {
        categoryService.create(category);
        return AddressRedirect.REDIRECT_CATEGORY;
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        categoryService.delete(id);
        return AddressRedirect.REDIRECT_CATEGORY;
    }

    @PostMapping("/update")
    public String update(@RequestBody CategoryUpdateDto categoryUpdateDto) throws ParseException {
        categoryService.update(categoryUpdateDto);
        return AddressRedirect.REDIRECT_CATEGORY;
    }

    @GetMapping("/list")
    public String redirect2Category(Model model, @RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber) {
        CategoryListDTO result = categoryService.findAllByPagination(
                CommonKey.STATUS,
                PageRequest.of(pageNumber,
                        10,
                        Sort.Direction.DESC,
                        CommonKey.ID)
        );

        model.addAttribute("page_type_en", AddressRedirect.CATEGORY);
        model.addAttribute("page_type_kh", AddressRedirect.CATEGORY_KH);
        model.addAttribute("content", result.getRecords());
        model.addAttribute("total_records", result.getRecords().size());
        model.addAttribute("total_pages", result.getTotalPages());
        model.addAttribute("current_page", result.getPageNumber());
        model.addAttribute("sort_by", result.getSortBy());
        model.addAttribute("sort_direction", result.getSortDirection());
        model.addAttribute("first", result.getFirst());
        model.addAttribute("last", result.getLast());

        return AddressRedirect.HOME;
    }
}
