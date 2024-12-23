package com.clothes.controller.customer;

import com.clothes.model.Account;
import com.clothes.model.Category;
import com.clothes.model.Product;
import com.clothes.model.Sale;
import com.clothes.repository.SalesRepository;
import com.clothes.service.ProductsService;
import com.clothes.service.SalesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomesController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SalesService salesService;

    @GetMapping
    public String home(Model model, HttpSession session) {
        var account = (Account) session.getAttribute("account");
        List<Sale> sales = salesRepository.findAll();
        Sale maxSale = sales.stream()
                .max(Comparator.comparing(Sale::getValue))
                .orElse(null);
        List<Product> products = productsService.getProductsBySaleId(maxSale.getId());
        model.addAttribute("email", account != null ? account.getEmail() : "Customer");
        model.addAttribute("products", products);
        model.addAttribute("sale", maxSale);
        return "customer/homepage";
    }
}
