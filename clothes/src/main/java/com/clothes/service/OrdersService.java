package com.clothes.service;

import com.clothes.model.Order;

public interface OrdersService {
    Order saveOrder(Order order);

    Order findOrderById(String id);
}