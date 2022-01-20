package com.teamproject.covid19vaccinereview.aop.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity IllegalArgumentExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.info("IllegalArgumentException: {}", e);
        log.info("request.getMethod(): " + request.getMethod());
        log.info("String.valueOf(response.getStatus()): " + response.getStatus());

        return ResponseEntity.badRequest().body("IllegalArgumentException test");
    }

}