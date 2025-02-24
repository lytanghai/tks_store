package com.group.tks_store.product.product.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.common.static_key.AddressRedirect;
import com.group.tks_store.common.static_key.CommonKey;
import com.group.tks_store.product.category.dto.CategoryDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import com.group.tks_store.product.product.dto.ProductCreateDTO;
import com.group.tks_store.product.product.dto.ProductListDTO;
import com.group.tks_store.product.product.dto.ProductUpdateDto;
import com.group.tks_store.product.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(CommonKey.API_CONTEXT_PATH + AddressRedirect.PRODUCT)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping(CommonKey.CREATE)
    public String create(@RequestBody ProductCreateDTO productCreateDTO) throws ParseException {
        productService.create(productCreateDTO);
        return AddressRedirect.REDIRECT_PRODUCT;
    }

    @PostMapping(CommonKey.DELETE)
    public String delete(@RequestBody ID id) {
        productService.delete(id);
        return AddressRedirect.REDIRECT_PRODUCT;
    }

    @PostMapping(CommonKey.UPDATE)
    public String update(@RequestBody ProductUpdateDto productUpdateDto) throws ParseException {
        productService.update(productUpdateDto);
        return AddressRedirect.REDIRECT_PRODUCT;
    }

    @GetMapping("/api/categories")
    @ResponseBody
    public List<CategoryDTO> getCategories() {
        List<CategoryDTO> list = new ArrayList<>();
        List<CategoryEntity> listEntity = new ArrayList<>();

        for(int i=0;i<listEntity.size();i++) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(listEntity.get(i).getId());
            categoryDTO.setName(listEntity.get(i).getName());
            list.set(i, categoryDTO);
        }
        return list;
    }

    @GetMapping(CommonKey.LIST)
    public String getActiveProducts(@RequestParam(name = CommonKey.PAGE, defaultValue = "0") Integer pageNumber,
                                    @RequestParam(name = CommonKey.SIZE, defaultValue = "10") Integer pageSize,
                                    @RequestParam(name = CommonKey.SORT, defaultValue = "id") String sortBy,
                                    @RequestParam(name = CommonKey.DIRECTION, defaultValue = "DESC") String sortDirection,
                                    Model model) {

        Page<ProductListDTO> productPage = productService.getActiveProducts(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.fromString(sortDirection),
                        sortBy))
        );

        model.addAttribute("page_type_en", AddressRedirect.PRODUCT);
        model.addAttribute("page_type_kh", AddressRedirect.PRODUCT_KH);
        model.addAttribute("content", productPage.getContent());
        model.addAttribute("total_records", productPage.getTotalElements());
        model.addAttribute("total_pages", productPage.getTotalPages());
        model.addAttribute("current_page", productPage.getNumber());
        model.addAttribute("sort_by", sortBy);
        model.addAttribute("sort_direction", sortDirection);
        model.addAttribute("first", productPage.isFirst());
        model.addAttribute("last", productPage.isLast());


        return AddressRedirect.HOME;
    }


}
