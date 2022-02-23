package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class AlreadyLikeException extends RuntimeException{
    public AlreadyLikeException(String message) {
        super(message);
    }
}
