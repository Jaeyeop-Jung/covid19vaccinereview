package com.teamproject.covid19vaccinereview.dto;


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

    private String googleId;

}
