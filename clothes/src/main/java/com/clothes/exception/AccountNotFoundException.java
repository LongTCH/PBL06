package com.clothes.exception;

public class AccountNotFoundException extends NotFoundExeception {
    public AccountNotFoundException() {
        super("Account not found");
    }
}
