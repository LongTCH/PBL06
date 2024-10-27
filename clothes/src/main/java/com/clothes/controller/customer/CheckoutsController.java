package com.clothes.controller.customer;

import com.clothes.config.VNPayService;
import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.Order;
import com.clothes.model.embedded.Address;
import com.clothes.model.embedded.OrderItem;
import com.clothes.service.OrdersService;
import com.clothes.service.ProductsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("CheckoutsController")
@RequestMapping("/checkouts")
public class CheckoutsController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private VNPayService vnPayService;

    @GetMapping
    public String getCheckoutPage(Model model) {

        return "customer/products/checkouts";
    }

    @PostMapping("/addOrder")
    public ResponseEntity<?> addOrder(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String phone = (String) body.get("phone");
        Map<String, Object> addressMap = (Map<String, Object>) body.get("address");
        Integer amount = (Integer) body.get("totalPrice");

        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        List<OrderItem> orderItems = items.stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId((String) item.get("productId"));
            orderItem.setProductVariantId((String) item.get("productVariantId"));
            orderItem.setPrice((int) item.get("price"));
            orderItem.setQuantity((int) item.get("quantity"));
            return orderItem;
        }).collect(Collectors.toList());

        Address customerAddress = new Address();
        customerAddress.setCity((String) addressMap.get("city"));
        customerAddress.setDistrict((String) addressMap.get("district"));
        customerAddress.setWard((String) addressMap.get("ward"));
        customerAddress.setStreet((String) addressMap.get("street"));
        Order order = new Order();
        order.setCustomerName(name);
        order.setCustomerEmail(email);
        order.setCustomerPhone(phone);
        order.setCustomerAddress(customerAddress);
        order.setItems(orderItems);
        order.setAmount(amount);
        order.setStatus(OrderStatusEnum.CREATED);
        ordersService.saveOrder(order);
        return ResponseEntity.ok(Map.of("orderId", order.getId(), "status", "success"));
    }

    @GetMapping("/success")
    public String orderConfirmation(Model model, @RequestParam("orderId") String orderId) {
        Order order = ordersService.findOrderById(orderId);
        model.addAttribute("order", order);
        return "customer/products/order-success";
    }
    @PostMapping("/submitOrder")
    public ResponseEntity<?> submitOrder(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        int orderTotal = (int) body.get("amount");
        String orderInfo = (String) body.get("orderInfo");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);


        Map<String, Object> orderData = (Map<String, Object>) body.get("data");
        addOrder(orderData);

        return ResponseEntity.ok(Map.of("redirectUrl", vnpayUrl));
    }


    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "customer/VNPay/ordersuccess" : "customer/VNPay/orderfail";
    }
}