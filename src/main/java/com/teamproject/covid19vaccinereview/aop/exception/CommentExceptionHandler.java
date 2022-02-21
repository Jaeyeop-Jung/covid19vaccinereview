package com.teamproject.covid19vaccinereview.aop.exception;

import com.teamproject.covid19vaccinereview.aop.exception.customException.CommentNotFoundException;
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
public class CommentExceptionHandler {

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Map<String, String>> commentNotFoundExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : commentNotFoundExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "해당 댓글을 찾지 못했습니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

}
