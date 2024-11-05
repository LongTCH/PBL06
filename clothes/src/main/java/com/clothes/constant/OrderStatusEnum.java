package com.clothes.constant;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    CREATED,
    PAID,
    CONFIRMED,
    CANCELLED;

    public String getVietnameseStatus() {
        switch (this) {
            case CREATED:
                return "Chờ xác nhận";
            case PAID:
                return "Đã thanh toán";
            case CONFIRMED:
                return "Đã xác nhận";
            case CANCELLED:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }
}

