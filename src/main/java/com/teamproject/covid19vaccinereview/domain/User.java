package com.teamproject.covid19vaccinereview.domain;

import lombok.*;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "loginprovider")
    private LoginProvider loginProvider;

    @NotNull
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILEIMAGE_ID")
    private ProfileImage profileImage;

    private String refreshToken;

    public User(String email, String password, UserRole role, LoginProvider loginProvider, String nickname, ProfileImage profileImage, String refreshToken) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.loginProvider = loginProvider;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.refreshToken = refreshToken;
    }

    public static User of(String email, String password, UserRole role, LoginProvider provider, String nickname, ProfileImage profileImage, String refreshToken){
        return new User(email, password, role, provider, nickname, profileImage, refreshToken);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeRefreshToken(String refreshToken){ this.refreshToken = refreshToken;}
}
