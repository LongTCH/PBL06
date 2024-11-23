package com.clothes.controller.admin;

import com.clothes.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("adminDashboardController")
@RequestMapping("/admin")
public class DashBoardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String getMethodName(Model model, HttpSession session) {
        int totalOrders = dashboardService.getTotalOrders();
        double totalRevenue = dashboardService.getTotalRevenue();
        int totalUsers = dashboardService.getTotalUsers();
        int todayOrders = dashboardService.getTodayOrders();
        List<Map<String, Object>> topCustomers = dashboardService.getTopCustomers();
        List<Map<String, Object>> topSellingProducts = dashboardService.getTopSellingProducts();
        List<String> productNames = topSellingProducts.stream()
                .map(product -> (String) product.get("productName"))
                .collect(Collectors.toList());

        List<Integer> unitsSold = topSellingProducts.stream()
                .map(product -> (Integer) product.get("quantitySold"))
                .collect(Collectors.toList());

        model.addAttribute("current_page", "dashboard_active");
        model.addAttribute("name", "Admin");
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("todayOrders", todayOrders);
        model.addAttribute("topCustomers", topCustomers);
        model.addAttribute("productNames", productNames);
        model.addAttribute("unitsSold", unitsSold);

        return "admin/home";
    }
}
