package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserModifyResponse {

    private String nickname;

    private String profileImageUrl;

    @Builder
    public UserModifyResponse(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
