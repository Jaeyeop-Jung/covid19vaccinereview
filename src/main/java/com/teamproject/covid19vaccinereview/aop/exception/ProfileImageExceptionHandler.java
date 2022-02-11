package com.teamproject.covid19vaccinereview.aop.exception;

import com.teamproject.covid19vaccinereview.aop.exception.customException.EmailDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageFileDuplicateException;
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
public class ProfileImageExceptionHandler {

    @ExceptionHandler(ProfileImageFileDuplicateException.class)
    public ResponseEntity<Map<String, String>> profileImageFileDuplicateExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : profileImageFileDuplicateExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "중복된 프로필 이미지 파일이 존재합니다. DB에 User가 없지만 프로필 이미지 파일이 존재하는지 확인해보십시오.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

    @ExceptionHandler(ProfileImageNotFoundException.class)
    public ResponseEntity<Map<String, String>> profileImageNotFoundExceptionHandler(Exception e){

        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("Advice : profileImageFileDuplicateExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "해당 프로필 이미지가 존재하지 않습니다.");

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }

}
