package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
