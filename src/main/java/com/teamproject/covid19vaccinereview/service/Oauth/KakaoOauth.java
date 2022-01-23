package com.teamproject.covid19vaccinereview.service.Oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth{

    @Value("${oauth.kakao.url}")
    private String KAKAO_AUTH_URL;

    @Value("${oauth.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${oauth.kakao.callback-url}")
    private String KAKAO_CALLBACK_URL;

    @Value("${oauth.kakao.token-url}")
    private String KAKAO_TOKEN_BASE_URL;

    @Value("${oauth.kakao.userinfo-url}")
    private String KAKAO_USERINFO_URL;

    @Override
    public String requestToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_TOKEN_BASE_URL, request, String.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return "카카오 로그인 요청 처리 실패";
        }

        return responseEntity.getBody();
    }

    @Override
    public MultiValueMap<String, Object> requestUserInfo(String accessToken) throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        HttpEntity request = new HttpEntity(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_URL, HttpMethod.GET, request, String.class);
//
//        JsonParser jsonParser = new JsonParser();
//        JsonElement parse = jsonParser.parse(response.getBody());
//
//        JsonElement kakaoNickname = parse.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname"); // 카카오 닉네임 가져오기
//        JsonElement kakaoProfileImageUrl = parse.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("thumbnail_image_url"); // 카카오 프로필 이미지  url 가져오기
//
//        byte[] imageBytes = urlToByteArray(kakaoProfileImageUrl.getAsString());
//        FileItem fileItem = new DiskFileItem()
//
//        MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();
//        userInfo.add("nickname", kakaoNickname + "#" + String.format("%04d", (int)(Math.random()*1000)));
//        userInfo.add("profileimage", imageBytes);

        return null;
    }

}
