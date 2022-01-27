package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException(String message) {
        super(message);
    }
}
