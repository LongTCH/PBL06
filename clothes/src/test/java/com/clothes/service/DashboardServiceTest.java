package com.clothes.service;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import com.clothes.model.Product;
import com.clothes.model.embedded.OrderItem;
import com.clothes.repository.AccountsRepository;
import com.clothes.repository.OrderRepository;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private ProductsRepository productRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(new Order(), new Order()));
        int totalOrders = dashboardService.getTotalOrders();
        assertEquals(2, totalOrders);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetTotalRevenue() {
        Order order1 = new Order();
        order1.setAmount(100);
        Order order2 = new Order();
        order2.setAmount(200);
        when(orderRepository.findByStatus(OrderStatusEnum.COMPLETED)).thenReturn(Arrays.asList(order1, order2));

        double totalRevenue = dashboardService.getTotalRevenue();

        assertEquals(300.0, totalRevenue);
        verify(orderRepository, times(1)).findByStatus(OrderStatusEnum.COMPLETED);
    }

    @Test
    void testGetTotalUsers() {
        when(accountsRepository.countByRole(AccountRolesEnum.CUSTOMER)).thenReturn(10);
        int totalUsers = dashboardService.getTotalUsers();
        assertEquals(10, totalUsers);
        verify(accountsRepository, times(1)).countByRole(AccountRolesEnum.CUSTOMER);
    }

    @Test
    void testGetTodayOrders() {
        LocalDate today = LocalDate.now();
        when(orderRepository.countByCreatedDateBetweenAndStatusNot(
                today.atStartOfDay(), today.atTime(23, 59, 59), OrderStatusEnum.CANCELLED)).thenReturn(5);
        int todayOrders = dashboardService.getTodayOrders();
        assertEquals(5, todayOrders);
        verify(orderRepository, times(1)).countByCreatedDateBetweenAndStatusNot(
                today.atStartOfDay(), today.atTime(23, 59, 59), OrderStatusEnum.CANCELLED);
    }

    @Test
    void testGetTopCustomers() {
        Order order1 = new Order();
        order1.setCustomerName("John");
        order1.setCustomerEmail("john@example.com");
        order1.setAmount(100);
        Order order2 = new Order();
        order2.setCustomerName("Jane");
        order2.setCustomerEmail("jane@example.com");
        order2.setAmount(200);
        when(orderRepository.findByStatus(OrderStatusEnum.COMPLETED)).thenReturn(Arrays.asList(order1, order2));
        List<Map<String, Object>> topCustomers = dashboardService.getTopCustomers();
        assertEquals(2, topCustomers.size());
        assertEquals("John", topCustomers.get(0).get("name"));
        assertEquals("Jane", topCustomers.get(1).get("name"));
        verify(orderRepository, times(1)).findByStatus(OrderStatusEnum.COMPLETED);
    }

    @Test
    void testGetTopSellingProducts() {
        OrderItem item1 = new OrderItem();
        item1.setProductId("prod1");
        item1.setQuantity(2);
        Product product1 = new Product();
        product1.setId("prod1");
        product1.setTitle("Product 1");
        item1.setProduct(product1);
        OrderItem item2 = new OrderItem();
        item2.setProductId("prod1");
        item2.setQuantity(3);
        Product product2 = new Product();
        product2.setId("prod1");
        product2.setTitle("Product 1");
        item2.setProduct(product2);
        Order order1 = new Order();
        order1.setItems(Arrays.asList(item1, item2));
        order1.setStatus(OrderStatusEnum.COMPLETED);
        when(orderRepository.findByStatus(OrderStatusEnum.COMPLETED)).thenReturn(Arrays.asList(order1));
        Product product = new Product();
        product.setId("prod1");
        product.setTitle("Product 1");
        when(productRepository.findById("prod1")).thenReturn(Optional.of(product));
        List<Map<String, Object>> topProducts = dashboardService.getTopSellingProducts();
        assertEquals(1, topProducts.size());
        assertEquals("Product 1", topProducts.get(0).get("productName"));
        assertEquals(5, topProducts.get(0).get("quantitySold"));  // 2 + 3 = 5
        verify(orderRepository, times(1)).findByStatus(OrderStatusEnum.COMPLETED);
        verify(productRepository, times(1)).findById("prod1");
    }

}
