package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class ProfileImageNotFoundException extends RuntimeException{
    public ProfileImageNotFoundException(String message) {
        super(message);
    }
}
