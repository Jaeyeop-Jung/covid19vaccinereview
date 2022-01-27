package com.teamproject.covid19vaccinereview.aop.exception;

import com.teamproject.covid19vaccinereview.aop.exception.customException.EmailDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.NicknameDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.NotDefineLoginProviderException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Map<String, String>> emailDuplicateExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : emailDuplicateExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "중복된 이메일이 존재합니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

    @ExceptionHandler(NicknameDuplicateException.class)
    public ResponseEntity<Map<String, String>> nicknameDuplicateExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : emailDuplicateExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "중복된 닉네임이 존재합니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotFoundExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : userNotFoundExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "아이디 또는 비밀번호가 잘못되었습니다");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

    @ExceptionHandler(NotDefineLoginProviderException.class)
    public ResponseEntity<Map<String, String>> notDefineLoginProviderExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : notDefineLoginProviderExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "정의되지 않은 LoginProvider입니다");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }
}
