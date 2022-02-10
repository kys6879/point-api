package com.musinsa.pointapi.advice.exception;

public class NotEnoughPointException extends IllegalStateException {
    public NotEnoughPointException(String message) {
        super(message);
    }
}
