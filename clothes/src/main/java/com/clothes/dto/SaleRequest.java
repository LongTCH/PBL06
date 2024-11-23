package com.clothes.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaleRequest {
    private String name;
    private double value;
    private String status;
    private List<String> productIds;
    private Boolean overrideConflict;
}