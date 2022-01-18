package com.teamproject.covid19vaccinereview.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.teamproject.covid19vaccinereview..*.*(..))")
    private static void advicePoint(){}

    @Before("advicePoint()")
    public void beforeParameterLog(JoinPoint joinPoint){
        Method method = getMethod(joinPoint);
        log.info("======= {}.{} =======", joinPoint.getTarget().getClass(), method.getName());

        Object[] args = joinPoint.getArgs();
        Map<String, String> params = new HashMap<>();
        for (Object arg : args) {
            if(arg != null)
                params.put(arg.getClass().toString(), arg.toString());
        }
        log.info("Parameter : " + params.toString());
    }

    @AfterReturning("advicePoint()")
    public void afterParameterLog(JoinPoint joinPoint){
        Method method = getMethod(joinPoint);
        log.info("======= {}.{} =======", joinPoint.getTarget().getClass(), method.getName());

        Object[] args = joinPoint.getArgs();
        Map<String, String> params = new HashMap<>();
        for (Object arg : args) {
            if(arg != null)
                params.put(arg.getClass().toString(), arg.toString());
        }
        log.info("Return : " + params.toString());
    }


    private Method getMethod(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
