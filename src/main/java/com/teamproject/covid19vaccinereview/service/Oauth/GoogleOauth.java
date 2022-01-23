package com.teamproject.covid19vaccinereview.service.Oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.Base64;
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

    @Value("${oauth.google.userinfo-url}")
    private String GOOGLE_USERINFO_URL;

    @Override
    public String requestToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("redirect_uri", GOOGLE_CALLBACK_URL);
        params.add("grant_type", "authorization_code");
        params.add("client_secret", GOOGLE_CLIENT_SECRET);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL, request, String.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return "구글 로그인 요청 처리 실패";
        }

        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(responseEntity.getBody());
        JsonObject jsonObject = (JsonObject) parse;

        String id_token = jsonObject.get("id_token").getAsString();

        return id_token;
    }

    @Override
    public MultiValueMap<String, Object> requestUserInfo(String oauthAccessToken) throws IOException {

        String[] split = oauthAccessToken.split("\\.");
        String decode = new String(Base64.getDecoder().decode(split[1]));

        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(decode);
        JsonObject jsonObject = (JsonObject) parse;

        String email = jsonObject.get("sub").getAsString();
        String nickname = jsonObject.get("name").getAsString();
        String profileImageUrl = jsonObject.get("picture").getAsString();

        byte[] imageBytes = urlToByteArray(profileImageUrl);

        MultipartFile multipartFile = new MockMultipartFile(email+".png", email+".png", "png",imageBytes);
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        System.out.println("multipartFile.getSize() = " + multipartFile.getSize());
        System.out.println("profileImageUrl = " + profileImageUrl);

        MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();
        userInfo.add("email", email);
        userInfo.add("nickname", nickname + "#" + String.format("%04d", (int)(Math.random()*1000)));
        userInfo.add("profileImage", multipartFile);
        userInfo.add("loginProvider", LoginProvider.GOOGLE);

        return userInfo;
    }
}
