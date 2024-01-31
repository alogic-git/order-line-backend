package com.orderline.basic.exception;

public class ExternalServerErrorException extends RuntimeException {

    private static final long serialVersionUID = 4702033203137521304L;

    public ExternalServerErrorException(String message) {
        super(message);
    }
}
