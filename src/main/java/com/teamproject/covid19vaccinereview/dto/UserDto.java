package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserDto {

    @NotNull
    private String email;

    @NotNull
    private String password;

    private UserRole role;

    @NotNull
    private String nickname;

    private String userPhoto;

    @Builder
    public UserDto(String email, String password, UserRole role, String nickname, String userPhoto) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.userPhoto = userPhoto;
    }

    public static User toEntity(String email, String password, String nickname, String userPhoto){
        return User.of(email, password, nickname, userPhoto);
    }

}
