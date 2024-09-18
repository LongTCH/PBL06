package com.clothes.model.embedded;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Map;

public class ProductVariant implements Serializable {
    @Id
    private String id;
    private int compareAtPrice;
    private int price;
    private int quantity;
    private String name;
    private boolean available;
    private Map<Integer, String> options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(int compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Map<Integer, String> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, String> options) {
        this.options = options;
    }

}
