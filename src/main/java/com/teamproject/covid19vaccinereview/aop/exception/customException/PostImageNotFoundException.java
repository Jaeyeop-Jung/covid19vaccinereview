package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class PostImageNotFoundException extends RuntimeException {
    public PostImageNotFoundException(String message) {
        super(message);
    }
}
