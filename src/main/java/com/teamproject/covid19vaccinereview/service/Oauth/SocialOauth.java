package com.teamproject.covid19vaccinereview.service.Oauth;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public interface SocialOauth {

    String requestToken(String authorizationCode);

    MultiValueMap<String, Object> requestUserInfo(String oauthAccessToken) throws IOException;

    default LoginProvider type() {
        if(this instanceof GoogleOauth){
            return LoginProvider.GOOGLE;
        } else if(this instanceof KakaoOauth){
            return LoginProvider.KAKAO;
        } else if(this instanceof NaverOauth){
            return LoginProvider.NAVER;
        }
        else{
            return null;
        }
    }

}
