package com.teamproject.covid19vaccinereview.filter;

import com.teamproject.covid19vaccinereview.aop.exception.customException.JwtIllegalArgumentException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            System.out.println("JwtExceptionFilter 동작");
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            System.out.println("eeror occur");
            response.getWriter().write("Ff");
        }
//        } catch () {
//
//        } catch () {
//
//        }
    }
}
