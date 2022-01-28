package com.teamproject.covid19vaccinereview.config;

import com.teamproject.covid19vaccinereview.aop.exception.CustomAccessDeniedHandler;
import com.teamproject.covid19vaccinereview.aop.exception.CustomEntryPoint;
import com.teamproject.covid19vaccinereview.filter.JwtAuthenticationFilter;
import com.teamproject.covid19vaccinereview.filter.JwtExceptionFilter;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가(SecurityConfig.class) 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true) // secure 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
//                .formLogin()
//                .loginPage("/login")
//                .usernameParameter("email") // UserDtoDetailsService에 loadUserByUsername의 파라미터 이름 설정
//                .loginProcessingUrl("/login") // /login URL이 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
//                .defaultSuccessUrl("/")// 특정 페이지("/random")에서 로그인페이지로 넘어와서 로그인을 하게되면 리턴을 "/"로 하는게 아니라 접근했던 특정 페이지("/random")로 반환을 해준다.
//                .and()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

    }
}
