package com.clothes.service.impl;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import com.clothes.repository.OrderRepository;
import com.clothes.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatusEnum orderStatusEnum) {
        return orderRepository.findByStatus(orderStatusEnum);
    }

    @Override
    public List<Order> getOrdersByAccountId(String customerName) {
        return orderRepository.findByCustomerEmail(customerName);
    }
}