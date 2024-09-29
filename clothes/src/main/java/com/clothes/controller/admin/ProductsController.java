package com.clothes.controller.admin;

import com.clothes.model.Product;
import com.clothes.service.CategoriesService;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller("adminProductsController")
@RequestMapping("/admin/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping
    public String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int page,
                                   @RequestParam(name = "size", defaultValue = "48") int size,
                                   @RequestParam(name = "categoryId", defaultValue = "all") String categoryId,
                                   @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                   Model model) {
        var categories = categoriesService.getAllCategories();
        var paginationResult = (Objects.equals(categoryId, "all"))
                ? productsService.getAllProductsWithSearch(page - 1, size, keyword)
                : productsService.getProductsByCategoryWithSearch(page - 1, size, categoryId, keyword);

        Map<String, String> categoryNames = new HashMap<>();
        for (Product product : paginationResult.getData()) {
            if (product.getCategoryId() != null) {
                String categoryName = categoriesService.getCategoryNameById(product.getCategoryId());
                categoryNames.put(product.getCategoryId(), categoryName);
            }
        }

        model.addAttribute("size", size);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        return "/admin/products/index";
    }
}
