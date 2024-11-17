package com.clothes.controller.seller;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.dto.OrderDetailsResponse;
import com.clothes.dto.OrderUpdateRequest;
import com.clothes.model.Order;
import com.clothes.model.Product;
import com.clothes.service.OrdersService;
import com.clothes.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Controller("sellerOrdersController")
@RequestMapping("/seller/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ProductsService productService;

    @RequestMapping
    public String list(@RequestParam(name = "orderStatus", required = false) String orderStatus,
                       Model model) {
        List<Order> orders;
        if (orderStatus != null && !orderStatus.isEmpty() && !"Tất cả".equals(orderStatus)) {
            try {
                orders = new ArrayList<>(ordersService.getOrdersByStatus(OrderStatusEnum.valueOf(orderStatus)));
            } catch (IllegalArgumentException e) {
                orders = new ArrayList<>(ordersService.getAllOrders());
            }
        } else {
            orders = new ArrayList<>(ordersService.getAllOrders());
        }
        List<OrderStatusEnum> orderStatuses = Arrays.stream(OrderStatusEnum.values()).collect(Collectors.toList());
        model.addAttribute("orderStatuses", orderStatuses);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("orders", orders);
        model.addAttribute("current_page", "order_active");

        return "seller/orders/list";
    }

    @GetMapping("/{id}/details")
    @ResponseBody
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable String id) {
        Order orderDetails = ordersService.findOrderById(id);
        List<Product> products = productService.findAllProducts();
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }
        if (orderDetails != null) {
            OrderDetailsResponse response = new OrderDetailsResponse(orderDetails, productMap);
            response.setNote(orderDetails.getSellerNote());
            response.setStatus(orderDetails.getStatus().name());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/update")
    @ResponseBody
    public ResponseEntity<Void> updateOrder(@PathVariable String id, @RequestBody OrderUpdateRequest request, Principal principal) {
        Order order = ordersService.findOrderById(id);
        if (order != null) {
            if ("confirm".equals(request.getAction())) {
                order.setStatus(OrderStatusEnum.CONFIRMED);
            } else if ("cancel".equals(request.getAction())) {
                order.setStatus(OrderStatusEnum.CANCELLED);
            } else if ("delivering".equals(request.getAction())) {
                order.setStatus(OrderStatusEnum.DELIVERING);
            } else if ("complete".equals(request.getAction())) {
                order.setStatus(OrderStatusEnum.COMPLETED);
            } else if ("refunded".equals(request.getAction())) {
                order.setStatus(OrderStatusEnum.REFUNDED);
            }

            order.setSellerNote(request.getNote());
            order.setSellerId(request.getSellerId());
            ordersService.saveOrder(order);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
