package com.teamproject.covid19vaccinereview.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class AopLogger {

    void logging() {
        log.info("info AopLogger class logging()");
        log.debug("debug AopLogger class logging()");
        log.error("error AopLogger class logging()");
    }


}
