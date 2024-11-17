package com.clothes.service;

import com.clothes.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface StatisticsService {
    Page<Order> findCompletedOrdersByDate(LocalDate date, Pageable pageable);

    List<Order> findCompletedOrdersByDate(LocalDate date);

    Page<Order> findCompletedOrdersByMonth(YearMonth month, Pageable pageable);

    List<Order> findCompletedOrdersByMonth(YearMonth month);

    long calculateRevenueByDate(LocalDate date);

    long calculateRevenueByMonth(YearMonth month);
}
