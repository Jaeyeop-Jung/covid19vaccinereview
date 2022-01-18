package com.teamproject.covid19vaccinereview.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "nickname"}))
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @NotNull
    private String nickname;

    private String userPhoto;

    private String googleId;

    private String refreshToken;

    private User(String email, String password, String nickname, String userPhoto, String googleId, String refreshToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userPhoto = userPhoto;
        this.googleId = googleId;
        this.refreshToken = refreshToken;
    }

    public static User of(String email, String password, String nickname, String userPhoto, String googleId, String refreshToken){
        return new User(email, password, nickname, userPhoto, googleId, refreshToken);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeGoogleId(String googleId){ this.googleId = googleId; }

    public void changeRefreshToken(String refreshToken){ this.refreshToken = refreshToken;}
}
