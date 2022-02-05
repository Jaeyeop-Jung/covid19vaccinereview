package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class PostContentBlankException extends RuntimeException{
    public PostContentBlankException(String message) {
        super(message);
    }
}
