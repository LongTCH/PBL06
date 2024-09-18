package com.clothes.model;

import com.clothes.model.embedded.CartItem;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "carts")
public class Cart implements Serializable {
    private List<CartItem> items;
    @Indexed(unique = true)
    private String accountId;
}
