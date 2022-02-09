package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private String nickname;

    private String profileImageUrl;

    @Builder
    public LoginResponse(String accessToken, String refreshToken, String nickname, String profileImageUrl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

}
