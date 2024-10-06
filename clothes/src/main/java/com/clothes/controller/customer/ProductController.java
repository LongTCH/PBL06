package com.clothes.controller.customer;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
import com.clothes.service.GroupsService;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller("customerProductController")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private GroupsService groupsService;

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<PaginationResultDto<Product>> search(@RequestParam(required = false) String title,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "24") int size) {
        var paginationResult = productsService.findProductsByTitle(title, page, size);

        return new ResponseEntity<>(paginationResult, HttpStatus.OK);
    }

    @GetMapping
    public String showProductsPage(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "24") int size) {
        var paginationResult = productsService.getAllProducts(page, size);
        var groups = groupsService.getAllGroups();
        model.addAttribute("groups", groups);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", page);
        return "/customer/products";
    }

    @GetMapping(value = "/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        Product product = productsService.findProductById(id);
        model.addAttribute("product", product);
        List<Product> relatedProducts = productsService.findRelatedProductsByCategoryId(product.getCategoryId());
        model.addAttribute("relatedProducts", relatedProducts);
        return "customer/products/ProductDetail";
    }

    @GetMapping(value = "/filter", produces = "text/html")
    public String filterProducts(
            @RequestParam(required = false) List<String> groupName,
            @RequestParam(required = false) List<String> sizeOption,
            @RequestParam(defaultValue = "0") int minPrice,
            @RequestParam(defaultValue = "10000000") int maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            Model model) {
        PaginationResultDto<Product> result = productsService.filterProducts(groupName, sizeOption, minPrice, maxPrice, page, size);
        model.addAttribute("products", result.getData());
        return "customer/products :: productsFragment";
    }

}
