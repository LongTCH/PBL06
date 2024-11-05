package com.clothes.controller.customer;

import com.clothes.model.Category;
import com.clothes.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller("customerCategoriesController")
@RequestMapping("/categories")
public class CategoriesController {
    @Autowired
    private CategoriesService categoryService;

    @GetMapping()
    public String getCategories(@RequestParam String groupId, Model model) {
        List<Category> categories = categoryService.getCategoryByGroupId(groupId);
        model.addAttribute("categories", categories);
        model.addAttribute("groupSelected", true);
        return "customer/products";
    }
}