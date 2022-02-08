package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginResponse {

    private String Authorization;

    private String refreshToken;

    private String nickname;

    private String profileImageURL;

    @Builder
    public LoginResponse(String authorization, String refreshToken, String nickname, String profileImageURL) {
        Authorization = authorization;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.profileImageURL = profileImageURL;
    }
}
