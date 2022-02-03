package com.teamproject.covid19vaccinereview.dto;


import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {

    private String email;

    private String password;

    private LoginProvider loginProvider;

    @Builder
    public LoginRequest(String email, String password, LoginProvider loginProvider) {
        this.email = email;
        this.password = password;
        this.loginProvider = loginProvider;
    }
}
