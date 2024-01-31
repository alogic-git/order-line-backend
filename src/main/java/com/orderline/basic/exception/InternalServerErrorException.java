package com.orderline.basic.exception;

public class InternalServerErrorException extends RuntimeException {

    private static final long serialVersionUID = -568386515345692156L;

    public InternalServerErrorException(String message) {
        super(message);
    }
}
