package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(String message) {
        super(message);
    }
}
