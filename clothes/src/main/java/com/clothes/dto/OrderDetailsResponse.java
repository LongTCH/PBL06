package com.clothes.dto;

import com.clothes.model.Order;
import com.clothes.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class OrderDetailsResponse {
    private final Order order;
    private final Map<String, Product> products;
    @Setter
    @Getter
    private String note;
    @Setter
    @Getter
    private String status;

    public OrderDetailsResponse(Order order, Map<String, Product> products) {
        this.order = order;
        this.products = products;
        this.status = order.getStatus().name();

    }
}
