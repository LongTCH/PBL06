package com.clothes.service;

import com.clothes.model.Order;
import com.clothes.repository.OrderRepository;
import com.clothes.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    private LocalDate testDate;
    private YearMonth testMonth;

    @BeforeEach
    public void setUp() {
        testDate = LocalDate.of(2024, 12, 25); // Sample date
        testMonth = YearMonth.of(2024, 12); // Sample month
    }

    @Test
    public void testFindCompletedOrdersByDate() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(any(), any()))
                .thenReturn(orders);

        List<Order> result = statisticsService.findCompletedOrdersByDate(testDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findOrdersCompleteByCreatedDateBetweenAndStatus(any(), any());
    }

    @Test
    public void testCalculateRevenueByDate() {
        Order order1 = new Order();
        order1.setAmount(100);
        Order order2 = new Order();
        order2.setAmount(150);
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findCompletedOrdersByCreatedDate(any(), any()))
                .thenReturn(orders);

        long revenue = statisticsService.calculateRevenueByDate(testDate);

        assertEquals(250L, revenue);  // 100 + 150 = 250
        verify(orderRepository, times(1)).findCompletedOrdersByCreatedDate(any(), any());
    }

    @Test
    void testFindCompletedOrdersByMonth() {
        // Given
        YearMonth month = YearMonth.of(2024, 12); // December 2024
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59, 999999999);

        // Mocking the behavior of the repository
        Order mockOrder1 = new Order(); // Create mock order objects as needed
        Order mockOrder2 = new Order(); // Add more mock orders if necessary
        List<Order> expectedOrders = List.of(mockOrder1, mockOrder2);

        when(orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(startDateTime, endDateTime))
                .thenReturn(expectedOrders);

        // When
        List<Order> actualOrders = statisticsService.findCompletedOrdersByMonth(month);

        // Then
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size()); // Assuming 2 orders are returned
        verify(orderRepository, times(1)).findOrdersCompleteByCreatedDateBetweenAndStatus(startDateTime, endDateTime);
    }

    @Test
    public void testCalculateRevenueByMonth() {
        Order order1 = new Order();
        order1.setAmount(100); // Thiết lập giá trị amount cho order1
        Order order2 = new Order();
        order2.setAmount(150); // Thiết lập giá trị amount cho order2
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findCompletedOrdersByCreatedDate(any(), any()))
                .thenReturn(orders);

        long revenue = statisticsService.calculateRevenueByMonth(testMonth);

        assertEquals(250L, revenue); // Kết quả là 100 + 150 = 250
        verify(orderRepository, times(1)).findCompletedOrdersByCreatedDate(any(), any());
    }


    @Test
    public void testFindCompletedOrdersByDateWithPagination() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);
        Page<Order> orderPage = new PageImpl<>(orders);

        Pageable pageable = mock(Pageable.class);
        when(orderRepository.findOrdersCompleteByCreatedDateBetweenAndStatus(any(), any(), eq(pageable)))
                .thenReturn(orderPage);

        Page<Order> result = statisticsService.findCompletedOrdersByDate(testDate, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(orderRepository, times(1)).findOrdersCompleteByCreatedDateBetweenAndStatus(any(), any(), eq(pageable));
    }
}
