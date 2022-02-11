package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ModifyUserResponse {

    private String nickname;

    private String profileImageUrl;

    @Builder
    public ModifyUserResponse(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
