package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class CommentNotMatchedException extends RuntimeException{
    public CommentNotMatchedException(String message) {
        super(message);
    }
}
