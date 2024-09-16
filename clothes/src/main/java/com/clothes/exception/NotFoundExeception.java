package com.clothes.exception;

public class NotFoundExeception extends RuntimeException {
    public NotFoundExeception(String message) {
        super(message);
    }
}
