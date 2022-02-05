package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyUserRequest {

    private String password;

    private String nickname;

    private boolean changeProfileImage;

    @Builder
    public ModifyUserRequest(String password, String nickname, boolean changeProfileImage) {
        this.password = password;
        this.nickname = nickname;
        this.changeProfileImage = changeProfileImage;
    }
}
