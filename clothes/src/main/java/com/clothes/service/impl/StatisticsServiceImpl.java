package com.clothes.service.impl;

import com.clothes.model.Order;
import com.clothes.repository.OrderRepository;
import com.clothes.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Page<Order> findCompletedOrdersByDate(LocalDate date, Pageable pageable) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);

        return orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(startOfDay, endOfDay, pageable);
    }

    @Override
    public List<Order> findCompletedOrdersByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);

        return orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(startOfDay, endOfDay);
    }

    @Override
    public Page<Order> findCompletedOrdersByMonth(YearMonth month, Pageable pageable) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59, 999999999);
        return orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(startDateTime, endDateTime, pageable);
    }

    @Override
    public List<Order> findCompletedOrdersByMonth(YearMonth month) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59, 999999999);
        return orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(startDateTime, endDateTime);
    }

    @Override
    public long calculateRevenueByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);
        List<Order> completedOrders = orderRepository.findCompletedOrdersByCreatedDate(startOfDay, endOfDay);
        return completedOrders.stream()
                .mapToLong(Order::getAmount)
                .sum();
    }

    @Override
    public long calculateRevenueByMonth(YearMonth month) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59, 999999999);
        List<Order> completedOrders = orderRepository.findCompletedOrdersByCreatedDate(startDateTime, endDateTime);
        return completedOrders.stream()
                .mapToLong(Order::getAmount)
                .sum();
    }

}
