package com.teamproject.covid19vaccinereview.dto;


import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginRequest {

    @ApiParam(required = true)
    @Email
    private String email;

    @ApiParam(required = true)
    @NotNull
    private String password;

    @ApiParam(required = true)
    @NotNull
    private LoginProvider loginProvider;

    @Builder
    public LoginRequest(String email, String password, LoginProvider loginProvider) {
        this.email = email;
        this.password = password;
        this.loginProvider = loginProvider;
    }
}
