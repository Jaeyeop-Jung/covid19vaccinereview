package com.teamproject.covid19vaccinereview.service.Oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth{



    @Override
    public String getOauthRedirectURL() {
        return null;
    }

    @Override
    public String requestAccessToken(String authorizationCode) {
        return null;
    }
}
