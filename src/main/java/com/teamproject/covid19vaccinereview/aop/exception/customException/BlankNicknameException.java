package com.teamproject.covid19vaccinereview.aop.exception.customException;

public class BlankNicknameException extends RuntimeException{
    public BlankNicknameException(String message) {
        super(message);
    }
}
