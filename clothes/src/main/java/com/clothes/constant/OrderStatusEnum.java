package com.clothes.constant;

public enum OrderStatusEnum {
    CREATED("created"), CONFIRMED("confirmed"), CANCELLED("cancelled"), DELIVERED("delivered");
    private final String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    public static OrderStatusEnum fromValue(String value) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value " + value);
    }

    public String getValue() {
        return value;
    }
}
