package com.clothes.model.embedded;

import java.io.Serializable;

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

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
