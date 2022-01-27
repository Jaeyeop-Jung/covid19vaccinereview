package com.teamproject.covid19vaccinereview.service.Oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.utils.UrlFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NaverOauth implements SocialOauth {

    @Value("${oauth.naver.url}")
    private String NAVER_AUTH_URL;

    @Value("${oauth.naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${oauth.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${oauth.naver.callback-url}")
    private String NAVER_CALLBACK_URL;

    @Value("${oauth.naver.token-url}")
    private String NAVER_TOKEN_BASE_URL;

    @Value("${oauth.naver.userinfo-url}")
    private String NAVER_USERINFO_URL;

    private final UrlFileUtil urlFileUtil;

    @Override
    public String requestToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(NAVER_TOKEN_BASE_URL, request, String.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return "네이버 로그인 요청 처리 실패";
        }

        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(responseEntity.getBody());
        JsonObject jsonObject = (JsonObject) parse;

        String accessToken = jsonObject.get("access_token").getAsString();

        System.out.println("accessToken = " + accessToken);

        return accessToken;
    }

    @Override
    public MultiValueMap<String, Object> requestUserInfo(String oauthAccessToken) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oauthAccessToken);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(NAVER_USERINFO_URL, HttpMethod.GET, request, String.class);

        System.out.println("response = " + response.getBody());

        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(response.getBody());
        JsonObject jsonObject = (JsonObject) parse;

        String email = jsonObject.get("response").getAsJsonObject().get("id").getAsString();
        String nickname = jsonObject.get("response").getAsJsonObject().get("nickname").getAsString();
        String profileImageUrl = null;
        MultipartFile multipartFile = null;
        if (jsonObject.get("response").getAsJsonObject().get("profile_image") != null) {

            profileImageUrl = jsonObject.get("response").getAsJsonObject().get("profile_image").getAsString();
            byte[] imageBytes = urlFileUtil.urlToByteArray(profileImageUrl);
            multipartFile = new MockMultipartFile(email + ".png", email + ".png", ".png", imageBytes);

        }

        MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();
        userInfo.add("email", email);
        userInfo.add("nickname", nickname + "#" + String.format("%04d", (int) (Math.random() * 1000)));
        userInfo.add("profileImage", multipartFile);
        userInfo.add("loginProvider", LoginProvider.NAVER);

        return userInfo;
    }
}
