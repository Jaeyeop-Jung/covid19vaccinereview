package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestUtil {

    public JoinRequest createJoinRequestWithUUID(String UUID){
        return JoinRequest.builder()
                .email(UUID)
                .password(UUID)
                .loginProvider(LoginProvider.ORIGINAL)
                .nickname(UUID)
                .build();
    }

    public LoginRequest createLoginReqeustWithUUID(String UUID){
        return LoginRequest.builder()
                .email(UUID)
                .password(UUID)
                .loginProvider(LoginProvider.ORIGINAL)
                .build();
    }

}
