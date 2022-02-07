package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class IncorrectDeleteUserRequestException extends RuntimeException{
    public IncorrectDeleteUserRequestException(String message) {
        super(message);
    }
}
