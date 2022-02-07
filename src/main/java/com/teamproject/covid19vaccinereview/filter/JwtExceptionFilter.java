package com.teamproject.covid19vaccinereview.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.covid19vaccinereview.aop.exception.customException.IncorrectDeleteUserRequestException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.JwtIllegalArgumentException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        try{
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {

        } catch (IncorrectDeleteUserRequestException e){
            Map<String, String> map = new HashMap<>();

            map.put("errortype", "Forbidden");
            map.put("code", "402");
            map.put("message", "잘못된 토큰 또는 이미 삭제된 회원입니다.");

            log.error("삭제된 유저의 토큰으로 접근");
            response.getWriter().write(objectMapper.writeValueAsString(map));
        }
//        } catch () {
//
//        } catch () {
//
//        }
    }
}
