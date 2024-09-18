package com.clothes.model;

import com.clothes.model.base.AuditableEntity;
import com.clothes.model.embedded.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order extends AuditableEntity {
    @Id
    private String id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Address customerAddress;
    private String customerNote;
    private String status;
    private int transportFee;
    private String transportOrderId;
    private int amount;
    private String accountId;
}
