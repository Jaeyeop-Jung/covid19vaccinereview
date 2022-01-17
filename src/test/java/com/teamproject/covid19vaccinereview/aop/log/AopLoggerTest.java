package com.teamproject.covid19vaccinereview.aop.log;

import org.junit.jupiter.api.Test;

class AopLoggerTest {

    @Test
    void slf4jLoggingTest() {
        AopLogger aopLogger = new AopLogger();
        aopLogger.logging();
    }

}