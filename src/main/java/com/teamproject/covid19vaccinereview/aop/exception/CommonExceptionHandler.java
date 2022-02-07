package com.teamproject.covid19vaccinereview.aop.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ParameterBindingException;
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
public class CommonExceptionHandler {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity IllegalArgumentExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
//        log.info("IllegalArgumentException: {}", e);
//        log.info("request.getMethod(): " + request.getMethod());
//        log.info("String.valueOf(response.getStatus()): " + response.getStatus());
//
//        return ResponseEntity.badRequest().body("IllegalArgumentException test");
//    }

    @ExceptionHandler(ParameterBindingException.class)
    public ResponseEntity<Map<String, String>> parameterBindingExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.PAYMENT_REQUIRED;

        log.info("Advice : parameterBindingExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "필수 파라미터를 다시 확인해주세요 : " + e.getMessage());

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

}