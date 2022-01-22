package com.teamproject.covid19vaccinereview.service.Oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth{

    @Value("${oauth.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${oauth.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${oauth.google.url}")
    private String GOOGLE_AUTH_URL;

    @Value("${oauth.google.callback-url}")
    private String GOOGLE_CALLBACK_URL;

    @Value("${oauth.google.token-url}")
    private String GOOGLE_TOKEN_BASE_URL;

    @Override
    public String requestAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();   // 구글의 경우 body를 json 형식으로 보내기 때문에 아래와 같은 방식 채택
        params.put("code", authorizationCode);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL, params, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity.getBody();
        }

        return "구글 로그인 요청 처리 실패";
    }

    @Override
    public String requestUserInfo(String accessToken) {
        return null;
    }
}
