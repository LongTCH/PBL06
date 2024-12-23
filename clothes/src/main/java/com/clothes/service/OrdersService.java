package com.clothes.service;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;

import java.util.List;

public interface OrdersService {
    Order saveOrder(Order order);

    Order findOrderById(String id);

    List<Order> getAllOrders();

    List<Order> getOrdersByStatus(OrderStatusEnum orderStatusEnum);

    List<Order> getOrdersByAccountId(String accountId);
}