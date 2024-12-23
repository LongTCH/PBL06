package com.clothes.service;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import com.clothes.repository.OrderRepository;
import com.clothes.service.impl.OrdersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
        order.getId();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatusEnum.CREATED);
    }

    @Test
    public void testSaveOrder() {
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = ordersService.saveOrder(order);

        assertNotNull(savedOrder);
        assertEquals(order.getId(), savedOrder.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testFindOrderById() {
        when(orderRepository.findById("1")).thenReturn(Optional.of(order));

        Order foundOrder = ordersService.findOrderById("1");

        assertNotNull(foundOrder);
        assertEquals(order.getId(), foundOrder.getId());
        verify(orderRepository, times(1)).findById("1");
    }

    @Test
    public void testFindOrderById_OrderNotFound() {
        when(orderRepository.findById("1")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> ordersService.findOrderById("1"));

        assertEquals("Order not found", thrown.getMessage());
        verify(orderRepository, times(1)).findById("1");
    }

    @Test
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<Order> orders = ordersService.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersByStatus() {
        when(orderRepository.findByStatus(OrderStatusEnum.CREATED)).thenReturn(Arrays.asList(order));

        List<Order> orders = ordersService.getOrdersByStatus(OrderStatusEnum.CREATED);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatusEnum.CREATED);
    }

    @Test
    public void testGetOrdersByAccountId() {
        when(orderRepository.findByCustomerEmail("customer@example.com")).thenReturn(Arrays.asList(order));

        List<Order> orders = ordersService.getOrdersByAccountId("customer@example.com");

        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByCustomerEmail("customer@example.com");
    }
}
