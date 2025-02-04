package com.clothes.controller.customer;

import com.clothes.dto.FindVariantByOptionsDto;
import com.clothes.model.Product;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("CartController")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductsService productsService;

    @GetMapping
    public String getCartPage(Model model) {

        return "customer/products/cart";
    }

    @GetMapping("/products")
    @ResponseBody
    public ResponseEntity<List<Product>> getProductsByIds(
            @RequestParam("id") List<String> ids) {

        List<Product> products = productsService.findProductByIds(ids);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/variants")
    @ResponseBody
    public ResponseEntity<ProductVariant> getVariantByIdAndName(
            @RequestBody FindVariantByOptionsDto dto) {
        ProductVariant variant = productsService.findVariant(dto);
        if (variant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(variant);
    }
}