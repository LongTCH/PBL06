package com.clothes.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderUpdateRequest {
    private String action;
    private String note;
    private String sellerId;
}
