package com.ptglue.basic.exception;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -122037772385408266L;

    public UnauthorizedException() {
        super("인증에 실패하였습니다.");
    }
}
