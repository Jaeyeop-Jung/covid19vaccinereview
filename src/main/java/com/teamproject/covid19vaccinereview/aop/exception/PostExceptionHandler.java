package com.teamproject.covid19vaccinereview.aop.exception;

import com.teamproject.covid19vaccinereview.aop.exception.customException.EmailDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostContentBlankException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostTitleBlankException;
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
public class PostExceptionHandler {

    @ExceptionHandler(PostTitleBlankException.class)
    public ResponseEntity<Map<String, String>> postTitleBlankExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : postTitleBlankExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "글의 제목이 비어있거나 공백으로만 이루어져 있습니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

    @ExceptionHandler(PostContentBlankException.class)
    public ResponseEntity<Map<String, String>> postContentBlankExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : postContentBlankExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "글의 내용이 비어있거나 공백으로만 이루어져 있습니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }
}
