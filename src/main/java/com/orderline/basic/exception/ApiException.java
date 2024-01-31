package com.orderline.basic.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1859572264636581247L;

    private final String systemTitle;

    public ApiException(String systemTitle) {
        this.systemTitle = systemTitle;
    }
}
