package com.teamproject.covid19vaccinereview.domain;

import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.ProfileImageDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

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
    private UserProvider provider;

    @NotNull
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILEIMAGE_ID")
    private ProfileImage profileImage;

    private String refreshToken;

    public User(String email, String password, UserRole role, UserProvider provider, String nickname, ProfileImage profileImage, String refreshToken) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.refreshToken = refreshToken;
    }

    public static User of(String email, String password, UserRole role, UserProvider provider, String nickname, ProfileImage profileImage, String refreshToken){
        return new User(email, password, role, provider, nickname, profileImage, refreshToken);
    }

    public static User from(JoinRequest joinRequest){

        ProfileImageDto profileImageDto = joinRequest.getProfileImageDto();

    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeRefreshToken(String refreshToken){ this.refreshToken = refreshToken;}
}
