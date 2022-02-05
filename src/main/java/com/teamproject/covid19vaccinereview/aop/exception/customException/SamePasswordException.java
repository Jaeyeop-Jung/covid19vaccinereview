package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class SamePasswordException extends RuntimeException{
    public SamePasswordException(String message) {
        super(message);
    }
}
