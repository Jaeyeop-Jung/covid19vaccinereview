package com.teamproject.covid19vaccinereview.service.Oauth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamproject.covid19vaccinereview.domain.LoginProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.teamproject.covid19vaccinereview.utils.UrlFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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

    @Value("${file.profileimagepath}")
    private String PROFILEIMAGE_PATH;

    private final UrlFileUtil urlFileUtil;

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
        String decode = new String(Base64Utils.decodeFromUrlSafeString(split[1]));

        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(decode);
        JsonObject jsonObject = (JsonObject) parse;

        String email = jsonObject.get("sub").getAsString();
        String nickname = jsonObject.get("name").getAsString();
        String profileImageUrl = jsonObject.get("picture").getAsString();

        byte[] imageBytes = urlFileUtil.urlToByteArray(profileImageUrl);

        File file = urlFileUtil.saveFileFromByteArray(imageBytes, PROFILEIMAGE_PATH + "/" + email + ".png");
        if((file == null)){
            throw new IOException("이미 같은 이름의 이미지가 있습니다.");
        }
        DiskFileItem diskFileItem = new DiskFileItem(email, ".png", false, file.getName(), imageBytes.length, new File(PROFILEIMAGE_PATH));
        diskFileItem.getOutputStream();

        MultipartFile multipartFile = new CommonsMultipartFile(diskFileItem);
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        System.out.println("multipartFile.getSize() = " + multipartFile.getSize());
        System.out.println("profileImageUrl = " + profileImageUrl);
        System.out.println("multipartFile.getBytes() = " + multipartFile.getBytes());

        MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();
        userInfo.add("email", email);
        userInfo.add("nickname", nickname + "#" + String.format("%04d", (int)(Math.random()*1000)));
        userInfo.add("profileImage", multipartFile);
        userInfo.add("loginProvider", LoginProvider.GOOGLE);

        return userInfo;
    }
}
