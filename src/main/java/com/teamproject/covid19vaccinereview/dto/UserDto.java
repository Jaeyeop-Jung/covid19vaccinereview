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

    private String email;

    private String password;

    private UserRole role;

    private String nickname;

    private String googleId;

    private String refreshToken;

    @Builder
    public UserDto(String email, String password, UserRole role, String nickname, String googleId, String refreshToken) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.googleId = googleId;
        this.refreshToken = refreshToken;
    }

    public static User toEntity(String email, String password, String nickname, String googleId, String refreshToken){
        return User.of(email, password, nickname, googleId, refreshToken);
    }

}
