package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class NicknameDuplicateException extends RuntimeException{

    public NicknameDuplicateException(String message) {
        super(message);
    }
}
