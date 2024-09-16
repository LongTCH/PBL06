package com.clothes.constant;

public enum AccountRolesEnum {
    ADMIN(0), SELLER(1), CUSTOMER(2);

    private final int value;

    AccountRolesEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AccountRolesEnum fromValue(int value) {
        for (AccountRolesEnum role : AccountRolesEnum.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant for value " + value);
    }
}