package com.example.DA.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public ApiException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ApiException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
