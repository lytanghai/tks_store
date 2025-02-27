package com.group.tks_store.product.attribute.controller;

import com.group.tks_store.common.dto.ID;
import com.group.tks_store.product.attribute.dto.AttributeCreateDTO;
import com.group.tks_store.product.attribute.dto.AttributeListDTO;
import com.group.tks_store.product.attribute.dto.AttributeUpdateDto;
import com.group.tks_store.product.attribute.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping("/attribute")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping("/create")
    public String create(@RequestBody AttributeCreateDTO attribute) throws ParseException {
        attributeService.create(attribute);
        System.out.println("calling to create attribute");
        return "redirect:/attribute";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody ID id) {
        attributeService.delete(id);
        return "redirect:/attribute";
    }

    @PostMapping("/update")
    public String delete(@RequestBody AttributeUpdateDto attributeUpdateDto) throws ParseException {
        attributeService.update(attributeUpdateDto);
        System.out.println("1 UPDATED");
        return "redirect:/attribute";
    }

    @GetMapping("/list")
    public String redirect2Attribute(Model model, @RequestParam(name = "page", defaultValue = "0") Integer pageNumber) {

        System.out.println("Calling Attribute List API");
        AttributeListDTO result = attributeService.findAllByPagination("ACTIVE", PageRequest.of(pageNumber, 10, Sort.Direction.DESC, "id"));

        model.addAttribute("page_type_en", "attribute");
        model.addAttribute("page_type_kh", "អង្គធាតុទំនិញ");
        model.addAttribute("content", result.getRecords());
        model.addAttribute("total_records", result.getRecords().size());
        model.addAttribute("total_pages", result.getTotalPages());
        model.addAttribute("current_page", result.getPageNumber());
        model.addAttribute("sort_by", result.getSortBy());
        model.addAttribute("sort_direction", result.getSortDirection());
        model.addAttribute("first", result.getFirst());
        model.addAttribute("last", result.getLast());

        return "home";
    }
}
