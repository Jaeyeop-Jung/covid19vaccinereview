package com.teamproject.covid19vaccinereview.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "nickname"}))
@Slf4j
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

    private User(String email, String password, String nickname, String userPhoto) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userPhoto = userPhoto;
    }

    public static User of(String email, String password, String nickname, String userPhoto){
        return new User(email, password, nickname, userPhoto);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

}
