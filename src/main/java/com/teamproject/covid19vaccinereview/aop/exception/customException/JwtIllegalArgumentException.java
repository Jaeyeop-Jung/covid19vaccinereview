package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class JwtIllegalArgumentException extends RuntimeException{
    public JwtIllegalArgumentException(String message) {
        super(message);
    }
}
