package com.teamproject.covid19vaccinereview.service.Oauth;

public class KakaoOauth implements SocialOauth{
    @Override
    public String getOauthRedirectURL() {
        return null;
    }

    @Override
    public String requestAccessToken(String authorizationCode) {
        return null;
    }
}
