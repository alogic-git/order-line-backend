package com.orderline.basic.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1858337435668947899L;

    public NotFoundException(String message) {
        super(message);
    }
}
