package com.clothes.service.impl;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import com.clothes.model.Product;
import com.clothes.model.embedded.OrderItem;
import com.clothes.repository.AccountsRepository;
import com.clothes.repository.OrderRepository;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private ProductsRepository productRepository;

    @Override
    public int getTotalOrders() {
        return orderRepository.findAll().size();
    }

    @Override
    public double getTotalRevenue() {
        List<Order> orders = orderRepository.findByStatus(OrderStatusEnum.COMPLETED);
        double totalRevenue = 0.0;
        for (Order order : orders) {
            totalRevenue += order.getAmount();
        }
        return totalRevenue;
    }

    @Override
    public int getTotalUsers() {
        return accountsRepository.countByRole(AccountRolesEnum.valueOf("CUSTOMER"));
    }

    @Override
    public int getTodayOrders() {
        LocalDate today = LocalDate.now();
        return orderRepository.countByCreatedDateBetweenAndStatusNot(
                today.atStartOfDay(), today.atTime(23, 59, 59), OrderStatusEnum.CANCELLED);
    }

    @Override
    public List<Map<String, Object>> getTopCustomers() {
        List<Order> orders = orderRepository.findByStatus(OrderStatusEnum.COMPLETED);
        Map<String, Integer> customerOrderCount = new HashMap<>();
        Map<String, Double> customerTotalSpend = new HashMap<>();
        Map<String, String> customerEmails = new HashMap<>();
        for (Order order : orders) {
            String customer = order.getCustomerName();
            String email = order.getCustomerEmail();
            customerEmails.put(customer, email);
            customerOrderCount.put(customer, customerOrderCount.getOrDefault(customer, 0) + 1);
            customerTotalSpend.put(customer, customerTotalSpend.getOrDefault(customer, 0.0) + order.getAmount());
        }

        List<Map<String, Object>> topCustomers = new ArrayList<>();
        for (String customer : customerOrderCount.keySet()) {
            Map<String, Object> customerInfo = new HashMap<>();
            customerInfo.put("name", customer);
            customerInfo.put("email", customerEmails.get(customer));
            customerInfo.put("orderCount", customerOrderCount.get(customer));
            customerInfo.put("totalSpend", customerTotalSpend.get(customer));
            topCustomers.add(customerInfo);
        }

        topCustomers.sort((c1, c2) -> ((Integer) c2.get("orderCount")).compareTo((Integer) c1.get("orderCount")));
        return topCustomers.size() > 5 ? topCustomers.subList(0, 5) : topCustomers;
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts() {
        List<Order> orders = orderRepository.findByStatus(OrderStatusEnum.COMPLETED);

        Map<String, Integer> productSalesCount = new HashMap<>();

        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                String productId = item.getProductId();
                productSalesCount.put(productId, productSalesCount.getOrDefault(productId, 0) + item.getQuantity());
            }
        }

        List<Map<String, Object>> topProducts = productSalesCount.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> productInfo = new HashMap<>();
                    productInfo.put("productId", entry.getKey());
                    productInfo.put("quantitySold", entry.getValue());
                    productInfo.put("productName", getProductNameById(entry.getKey()));
                    return productInfo;
                })
                .sorted((p1, p2) -> Integer.compare((Integer) p2.get("quantitySold"), (Integer) p1.get("quantitySold")))
                .limit(5)
                .collect(Collectors.toList());
        return topProducts;
    }

    private String getProductNameById(String productId) {
        return productRepository.findById(productId)
                .map(Product::getTitle)
                .orElse("Unknown Product");
    }
}
