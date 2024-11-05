package com.clothes.model.embedded;

import com.clothes.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderItem implements Serializable {
    private String productId;
    private String productVariantId;
    private int price;
    private int quantity;
    private Product product;
}
