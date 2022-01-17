package com.teamproject.covid19vaccinereview.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class AopLogger {

    // PointCut 은 적용할 패키지/메서드 등 위치
    @Pointcut("within(com.teamproject.covid19vaccinereview.api.*)")
    private void elapsedTimeLoggingPointCut() { }

    @Pointcut("execution(* com.teamproject.covid19vaccinereview.api.UserApiController.login())")
    private void loginUserInterceptorPointCut() { }

    // Around / Before / After / AfterThrowing 등 대상에 대해 동작시킬 전후 위치
    @Around("elapsedTimeLoggingPointCut()")
    public void elapsedTimeLogging() {
        System.out.println("Around elapsedTimeLogging()");
        log.info("Around elapsedTimeLogging()");
    }

    @Before("loginUserInterceptorPointCut()")
    public void loginUserInterceptor() {
        System.out.println("Before loginUserInterceptor()");
        log.info("Before loginUserInterceptor()");
    }

    void logging() {
        log.info("info AopLogger class logging()");
        log.debug("debug AopLogger class logging()");
        log.error("error AopLogger class logging()");
    }


}
