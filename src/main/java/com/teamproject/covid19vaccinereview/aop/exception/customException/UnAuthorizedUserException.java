package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class UnAuthorizedUserException extends RuntimeException{
    public UnAuthorizedUserException(String message) {
        super(message);
    }
}
