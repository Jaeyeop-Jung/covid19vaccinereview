package com.teamproject.covid19vaccinereview.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 프론트와 연동시 발생하는 CORS 애러를 해결한다.
 * 개발단계의 편의성을 위해 모든 메서드와 포트를 OPEN한다.
 *
 * TODO 실서버(master)브랜치에 업로드할땐 모든 설정을 명확히 설정해준다.
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3000;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}