package com.clothes.constant;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    CREATED,
    PAID,
    CONFIRMED,
    DELIVERING,
    CANCELLED,
    REFUNDED,
    COMPLETED;

    public String getVietnameseStatus() {
        switch (this) {
            case CREATED:
                return "Chờ xác nhận";
            case PAID:
                return "Đã thanh toán";
            case CONFIRMED:
                return "Đã Xác nhận";
            case DELIVERING:
                return "Đang giao hàng";
            case CANCELLED:
                return "Đã hủy";
            case COMPLETED:
                return "Hoàn thành";
            case REFUNDED:
                return "Trả hàng";
            default:
                return "Không xác định";
        }
    }
}

