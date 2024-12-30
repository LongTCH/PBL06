package com.clothes.repository;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface OrderRepository extends MongoRepository<Order, String> {
    Page<Order> findByCustomerNameContaining(String keyword, Pageable pageable);

    Page<Order> findByCustomerNameContainingAndCreatedDateBetweenAndStatusContaining(
            String keyword, LocalDate fromDate, LocalDate toDate, String statusValue, Pageable pageable);

    Page<Order> findByCustomerNameContainingAndCreatedDateBetween(String keyword, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    List<Order> findByStatus(OrderStatusEnum orderStatusEnum);

    int countByCreatedDateBetweenAndStatusNot(LocalDateTime localDateTime, LocalDateTime localDateTime1, OrderStatusEnum orderStatusEnum);

    @Query("{'createdDate' : { $gte: ?0, $lt: ?1 }, 'status': 'COMPLETED'}")
    Page<Order> findOrdersCompleteByCreatedDateBetweenAndStatus(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

    @Query("{'createdDate' : { $gte: ?0, $lt: ?1 }, 'status': 'COMPLETED'}")
    List<Order> findOrdersCompleteByCreatedDateBetweenAndStatus(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("{'createdDate' : { $gte: ?0, $lt: ?1 }, 'status': 'COMPLETED'}")
    List<Order> findCompletedOrdersByCreatedDate(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Page<Order> findByCustomerEmail(String customerName, Pageable pageable);
}
