package com.clothes.customer.Controllers;

import com.clothes.controller.customer.CheckoutsController;
import com.clothes.service.OrdersService;
import com.clothes.config.VNPayService;
import com.clothes.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CheckoutControllerTest {

    @InjectMocks
    private CheckoutsController checkoutsController;

    @Mock
    private OrdersService ordersService;

    @Mock
    private VNPayService vnPayService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutsController).build();
    }

    @Test
    void testGetCheckoutPage() throws Exception {
        mockMvc.perform(get("/checkouts"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/products/checkouts"));
    }
    @Test
    void testOrderConfirmation() {
        String orderId = "12345";
        Order mockOrder = new Order();
        mockOrder.setId(orderId);

        when(ordersService.findOrderById(orderId)).thenReturn(mockOrder);

        String viewName = checkoutsController.orderConfirmation(model, orderId);
        assertEquals("customer/products/order-success", viewName);
        verify(model).addAttribute("order", mockOrder);
    }

    @Test
    void testGetMapping_VNPAY_Payment() {
        when(vnPayService.orderReturn(request)).thenReturn(0);

        String viewName = checkoutsController.GetMapping(request, model);
        assertEquals("customer/VNPay/orderfail", viewName);
    }
}
