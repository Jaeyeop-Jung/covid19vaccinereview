package com.teamproject.covid19vaccinereview.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyUserRequest {

    private String password;

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
