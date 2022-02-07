package com.teamproject.covid19vaccinereview.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyUserRequest {

    private String password;

    private String nickname;

    @ApiParam(required = true)
    private boolean wantToChangeProfileImage;

    @Builder
    public ModifyUserRequest(String password, String nickname, boolean wantToChangeProfileImage) {
        this.password = password;
        this.nickname = nickname;
        this.wantToChangeProfileImage = wantToChangeProfileImage;
    }
}
