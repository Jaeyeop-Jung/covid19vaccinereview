package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message) {
        super(message);
    }
}
