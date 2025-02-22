package com.group.tks_store.product.products.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.category.dto.CategoryDTO;
import com.group.tks_store.product.category.entity.CategoryEntity;
import com.group.tks_store.product.category.service.CategoryService;
import com.group.tks_store.product.products.dto.ProductCreateDTO;
import com.group.tks_store.product.products.dto.ProductListDTO;
import com.group.tks_store.product.products.dto.ProductUpdateDto;
import com.group.tks_store.product.products.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String create(@RequestBody ProductCreateDTO productCreateDTO) throws ParseException {
        productService.create(productCreateDTO);
        return "redirect:/product";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        productService.delete(id);
        return "redirect:/product";
    }

    @PostMapping("/update")
    public String delete(@RequestBody ProductUpdateDto productUpdateDto) throws ParseException {
        productService.update(productUpdateDto);
        System.out.println("1 UPDATED");
        return "redirect:/product";
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

    @GetMapping("/list")
    public String getActiveProducts(@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
                                    @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
                                    @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                    @RequestParam(name = "direction", defaultValue = "DESC") String sortDirection,
                                    Model model) {
        System.out.println("Calling Product List API");

        Page<ProductListDTO> productPage = productService.getActiveProducts(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.by(Sort.Direction.fromString(sortDirection),
                        sortBy))
        );

        model.addAttribute("page_type_en", "product");
        model.addAttribute("page_type_kh", "ផលិតផល");
        model.addAttribute("content", productPage.getContent());
        model.addAttribute("total_records", productPage.getTotalElements());
        model.addAttribute("total_pages", productPage.getTotalPages());
        model.addAttribute("current_page", productPage.getNumber());
        model.addAttribute("sort_by", sortBy);
        model.addAttribute("sort_direction", sortDirection);
        model.addAttribute("first", productPage.isFirst());
        model.addAttribute("last", productPage.isLast());

        return "home";
    }


}
