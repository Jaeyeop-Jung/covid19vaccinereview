package com.teamproject.covid19vaccinereview.service.Oauth;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;

public interface SocialOauth {

    String getOauthRedirectURL();

    String requestAccessToken(String authorizationCode);

    default LoginProvider type() {
        if(this instanceof GoogleOauth){
            return LoginProvider.GOOGLE;
        } else if(this instanceof KakaoOauth){
            return LoginProvider.KAKAO;
        } else{
            return null;
        }
    }
}
