package com.clothes.controller.customer;

import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Product;
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
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", page);
        return "/customer/products";
    }

    @GetMapping(value = "/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        Product product = productsService.findProductById(id);
        model.addAttribute("product", product);
        List<Product> relatedProducts = productsService.findRelatedProductsByGroupId(product.getGroupId());
        model.addAttribute("relatedProducts", relatedProducts);
        return "customer/products/ProductDetail";
    }
}
