package com.teamproject.covid19vaccinereview.service.Oauth;

public interface SocialOauth {

    String getOauthRedirectURL();

    String requestAccessToken(String authorizationCode);
}
