package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.UserProvider;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class JoinRequest {

    private String email;

    private String password;

    private UserProvider provider;

    private String nickname;

    private ProfileImageDto profileImageDto;

    private String googleId;

}
