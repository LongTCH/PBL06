package com.clothes.controller.customer;

import com.clothes.model.Account;
import com.clothes.model.Category;
import com.clothes.model.Product;
import com.clothes.service.ProductsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomesController {
    @Autowired
    private ProductsService productsService;
    @GetMapping
    public String home(Model model, HttpSession session) {
        var account = (Account) session.getAttribute("account");
        List<Product> products = productsService.getAllProducts( 0, 12).getData();
        model.addAttribute("name", account != null ? account.getEmail() : "Guest");
        model.addAttribute("products", products);
        return "customer/homepage";
    }
}
