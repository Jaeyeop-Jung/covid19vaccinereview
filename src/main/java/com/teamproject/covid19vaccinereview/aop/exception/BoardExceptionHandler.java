package com.teamproject.covid19vaccinereview.aop.exception;

import com.teamproject.covid19vaccinereview.aop.exception.customException.EmailDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.IncorrcetBaordException;
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
public class BoardExceptionHandler {

    @ExceptionHandler(IncorrcetBaordException.class)
    public ResponseEntity<Map<String, String>> incorrcetBaordExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : incorrcetBaordExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "잘못된 게시판입니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

}
