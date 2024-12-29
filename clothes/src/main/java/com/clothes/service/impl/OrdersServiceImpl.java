package com.clothes.service.impl;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.dto.PaginationResultDto;
import com.clothes.model.Order;
import com.clothes.repository.OrderRepository;
import com.clothes.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PaginationResultDto<Order> getOrdersByAccountId(String customerName, int page, int size) {
        var pageOrders = orderRepository.findByCustomerEmail(customerName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
        var orders = pageOrders.getContent();
        return new PaginationResultDto<>(orders, page, pageOrders.getTotalPages(), pageOrders.getTotalElements());
    }
}