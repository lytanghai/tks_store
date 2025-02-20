package com.group.tks_store.homepage;

import com.group.tks_store.product.category.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {
    private final Logger log = LoggerFactory.getLogger(HomepageController.class);

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page_type_kh", "ទំព័រដើម");
        model.addAttribute("page_type_en", "home");
        return "home";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("page_type_kh", "ទំព័រដើម");
        model.addAttribute("page_type_en", "product");
        return "index";
    }

}
