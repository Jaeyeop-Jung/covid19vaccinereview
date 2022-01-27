package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class IncorrectRequestTokenException extends RuntimeException{
    public IncorrectRequestTokenException(String message) {
        super(message);
    }
}
