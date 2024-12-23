package com.clothes.controller.customer;

import com.clothes.model.Sale;
import com.clothes.service.ProductsService;
import com.clothes.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("customerSalesController")
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @Autowired
    private ProductsService productsService;

    @GetMapping(value = "/list", produces = "application/json")
    @ResponseBody
    public List<Sale> list() {
        return salesService.getActiveSales();
    }

    @GetMapping("{saleId}")
    public String showSale(@PathVariable String saleId,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "24") int size,
                           Model model) {
        var sale = salesService.get(saleId);
        var paginationResult = productsService.getProductsPaginationBySaleId(saleId, page, size);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", 0);
        model.addAttribute("sale", sale);
        return "customer/sales_view";
    }

    @PostMapping(value = "{saleId}", produces = "text/html")
    public String showSalePost(@PathVariable String saleId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "24") int size,
                               Model model) {
        var paginationResult = productsService.getProductsPaginationBySaleId(saleId, page, size);
        model.addAttribute("products", paginationResult.getData());
        model.addAttribute("pagination", paginationResult);
        model.addAttribute("currentPage", 0);
        return "customer/products/products-categories :: productsFragment";
    }
}
