package com.clothes.service;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    int getTotalOrders();

    double getTotalRevenue();

    int getTotalUsers();

    int getTodayOrders();

    List<Map<String, Object>> getTopCustomers();

    List<Map<String, Object>> getTopSellingProducts();
}
