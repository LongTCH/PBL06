package com.clothes.controller.customer;

import com.clothes.model.Product;
import com.clothes.model.embedded.ProductVariant;
import com.clothes.service.CartService;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("CartController")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

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

    @GetMapping("/variants")
    @ResponseBody
    public ResponseEntity<ProductVariant> getVariantByIdAndName(
            @RequestParam("id") String id,
            @RequestParam("variantName") String variantName) {
        Product product = productsService.findProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ProductVariant variant = product.getVariants().stream()
                .filter(v -> v.getName().equals(variantName))
                .findFirst()
                .orElse(null);

        if (variant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(variant);
    }

}