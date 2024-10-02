package com.clothes.model.embedded;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CartItem implements Serializable {
    private String productId;
    private String productVariantId;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String productId, String productVariantId, int quantity) {
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
    }

}
