package com.teamproject.covid19vaccinereview.aop.exception;

import com.teamproject.covid19vaccinereview.aop.exception.customException.PostImageNotFoundException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageNotFoundException;
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
public class PostImageExceptionHandler {

    @ExceptionHandler(PostImageNotFoundException.class)
    public ResponseEntity<Map<String, String>> postImageNotFoundExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : postImageNotFoundExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "해당 게시글 이미지가 존재하지 않습니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

}
