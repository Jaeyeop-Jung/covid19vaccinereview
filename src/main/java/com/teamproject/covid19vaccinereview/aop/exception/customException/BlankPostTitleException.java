package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class BlankPostTitleException extends RuntimeException{
    public BlankPostTitleException(String message) {
        super(message);
    }
}
