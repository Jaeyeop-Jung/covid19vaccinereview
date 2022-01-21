package com.teamproject.covid19vaccinereview.dto;


import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginRequest {

    private String email;

    private String password;

    private LoginProvider loginProvider;

    private String authorizationCode;

}
