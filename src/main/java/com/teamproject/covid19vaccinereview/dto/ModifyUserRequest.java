package com.teamproject.covid19vaccinereview.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyUserRequest {

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotNull
    @ApiParam(required = true)
    private boolean wantToChangeProfileImage;

    @Builder
    public ModifyUserRequest(String password, String nickname, boolean wantToChangeProfileImage) {
        this.password = password;
        this.nickname = nickname;
        this.wantToChangeProfileImage = wantToChangeProfileImage;
    }
}
