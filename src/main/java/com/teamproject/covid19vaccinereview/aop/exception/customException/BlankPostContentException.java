package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class BlankPostContentException extends RuntimeException{
    public BlankPostContentException(String message) {
        super(message);
    }
}
