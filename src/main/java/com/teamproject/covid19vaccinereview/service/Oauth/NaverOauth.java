package com.teamproject.covid19vaccinereview.service.Oauth;

import org.springframework.util.MultiValueMap;

import java.io.IOException;

public class NaverOauth implements SocialOauth {
    @Override
    public String requestToken(String authorizationCode) {
        return null;
    }

    @Override
    public MultiValueMap<String, Object> requestUserInfo(String oauthAccessToken) throws IOException {
        return null;
    }
}
