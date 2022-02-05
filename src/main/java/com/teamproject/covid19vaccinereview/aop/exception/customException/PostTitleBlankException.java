package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class PostTitleBlankException extends RuntimeException{
    public PostTitleBlankException(String message) {
        super(message);
    }
}
