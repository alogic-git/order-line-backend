package com.ptglue.basic.exception;

public class DuplicateException extends RuntimeException{
    private static final long serialVersionUID = -1536740116170754645L;
    public DuplicateException(String message) {
        super(message);
        }
}
