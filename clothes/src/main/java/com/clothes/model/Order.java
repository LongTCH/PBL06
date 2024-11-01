package com.clothes.model;

import com.clothes.constant.OrderStatusEnum;
import com.clothes.model.base.AuditableEntity;
import com.clothes.model.embedded.Address;
import com.clothes.model.embedded.OrderItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "orders")
public class Order extends AuditableEntity {
    @Id
    private String id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Address customerAddress;
    private String customerNote;
    private OrderStatusEnum status;
    private int transportFee;
    private String transportOrderId;
    private int amount;
    private String accountId;
    private List<OrderItem> items;
    private String sellerId;
    private String sellerNote;
}
