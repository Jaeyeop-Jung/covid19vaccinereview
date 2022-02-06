package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class BlankPasswordException extends RuntimeException{
    public BlankPasswordException(String message) {
        super(message);
    }
}
