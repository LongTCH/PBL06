package com.clothes.model.embedded;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ProductVariant implements Serializable {
    @Id
    private String id;
    private int compareAtPrice;
    private int price;
    private int quantity;
    private String name;
    private boolean available;
    private Map<Integer, String> options;

    public ProductVariant() {
        this.options = new HashMap<>();
    }

}
