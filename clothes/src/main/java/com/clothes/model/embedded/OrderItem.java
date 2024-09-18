package com.clothes.model.embedded;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String productId;
    private String productVariantId;
    private int price;
    private int quantity;
}
