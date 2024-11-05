package com.clothes.controller.admin;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.dto.ToastMessage;
import com.clothes.model.Order;
import com.clothes.model.Product;
import com.clothes.model.embedded.OrderItem;
import com.clothes.repository.OrderRepository;
import com.clothes.repository.ProductsRepository;
import com.clothes.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller("adminOrdersController")
@RequestMapping("/admin/orders")
public class OrdersController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductsRepository productRepository;
    @Autowired
    private OrdersService ordersService;

    @RequestMapping
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       @RequestParam(name = "sortOrder", defaultValue = "0") int sortOrder,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword,
                       @RequestParam(name = "fromDate", required = false) LocalDate fromDate,
                       @RequestParam(name = "toDate", required = false) LocalDate toDate,
                       @RequestParam(name = "orderStatus", required = false) String orderStatus,
                       Model model) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate) || fromDate != null && toDate != null && fromDate.isEqual(toDate)) {
            model.addAttribute("toastMessages", new ToastMessage("error", "Ngày bắt đầu phải trước ngày kết thúc"));
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortOrder == 1 ? Sort.Order.desc("createdDate") : Sort.Order.asc("createdDate")));
            Page<Order> pagination = orderRepository.findByCustomerNameContaining(keyword, pageable);

            model.addAttribute("orders", pagination.getContent());
            model.addAttribute("pagination", pagination);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("sortOrder", sortOrder);
            model.addAttribute("keyword", keyword);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("orderStatus", orderStatus);
            model.addAttribute("orderStatuses", OrderStatusEnum.values());
            model.addAttribute("current_page", "order_active");

            return "admin/orders/list";
        }

        Sort sort = Sort.by(sortOrder == 1 ? Sort.Order.desc("createdDate") : Sort.Order.asc("createdDate"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Order> pagination = null;

        if (fromDate != null && toDate != null) {
            if (orderStatus != null) {
                pagination = orderRepository.findByCustomerNameContainingAndCreatedDateBetweenAndStatusContaining(
                        keyword, fromDate, toDate, orderStatus, pageable);
                System.out.println(orderStatus);
            } else {
                pagination = orderRepository.findByCustomerNameContainingAndCreatedDateBetween(
                        keyword, fromDate, toDate, pageable);
            }
        } else {
            pagination = orderRepository.findByCustomerNameContaining(keyword, pageable);
        }

        model.addAttribute("orders", pagination.getContent());
        model.addAttribute("pagination", pagination);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("keyword", keyword);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("orderStatuses", OrderStatusEnum.values());
        model.addAttribute("current_page", "order_active");


        return "admin/orders/list";
    }

    @RequestMapping("/{id}/details")
    public String viewOrderDetails(@PathVariable("id") String orderId, Model model) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + item.getProductId()));
            item.setProduct(product);
        }
        model.addAttribute("order", order);

        return "admin/orders/detail";
    }

    @RequestMapping("/{id}/edit")
    public String editOrder(@PathVariable("id") String orderId, Model model) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        model.addAttribute("order", order);
        model.addAttribute("orderStatuses", OrderStatusEnum.values());
        return "admin/orders/edit";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(@PathVariable("id") String orderId, @RequestParam("orderStatus") String orderStatus) {
        Order order = ordersService.findOrderById(orderId);
        order.setStatus(OrderStatusEnum.valueOf(orderStatus));
        orderRepository.save(order);
        return "redirect:/admin/orders";
    }

}
