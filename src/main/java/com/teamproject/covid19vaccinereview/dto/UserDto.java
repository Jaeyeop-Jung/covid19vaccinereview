package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserDto {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private UserRole role;

    @NotNull
    private String nickname;

    private String userPhoto;

    public static User toEntity(String email, String password, UserRole role, String nickname, String userPhoto){
        return User.of(email, password, role, nickname, userPhoto);
    }

}
