package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class PostLostUserConnectionException extends RuntimeException{
    public PostLostUserConnectionException(String message) {
        super(message);
    }
}
