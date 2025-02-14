package com.group.tks_store.homepage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("page_type", "ទំព័រដើម");
        model.addAttribute("categories",null);
        model.addAttribute("products", null);
        return "home";
    }

    @GetMapping("/category")
    public String redirect2Category() {
        return "category";
    }
}
