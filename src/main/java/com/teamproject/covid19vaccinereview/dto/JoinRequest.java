package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.UserRole;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class JoinRequest {

    private String email;

    private String password;

    private String nickname;

    private String userPhoto;

    private String googleId;

    @Builder
    public JoinRequest(String email, String password, String nickname, String userPhoto, String googleId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userPhoto = userPhoto;
        this.googleId = googleId;
    }
}
