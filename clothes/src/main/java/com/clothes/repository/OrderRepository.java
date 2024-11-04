package com.clothes.repository;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends MongoRepository<Order, String> {
    Page<Order> findByCustomerNameContaining(String keyword, Pageable pageable);
    Page<Order> findByCustomerNameContainingAndCreatedDateBetweenAndStatusContaining(
            String keyword, LocalDate fromDate, LocalDate toDate, String statusValue, Pageable pageable);

    Page<Order> findByCustomerNameContainingAndCreatedDateBetween(String keyword, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    List<Order> findByStatus(OrderStatusEnum orderStatusEnum);
}
