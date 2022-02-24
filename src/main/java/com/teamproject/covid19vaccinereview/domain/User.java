package com.teamproject.covid19vaccinereview.domain;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "nickname"}))
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword()) && getRole() == user.getRole() && getLoginProvider() == user.getLoginProvider() && Objects.equals(getNickname(), user.getNickname()) && Objects.equals(getRefreshToken(), user.getRefreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getRole(), getLoginProvider(), getNickname(), getRefreshToken());
    }

    private User(String email, String password, UserRole role, LoginProvider loginProvider, String nickname, ProfileImage profileImage, String refreshToken) {
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

    protected static void validateEmailFormat(String email) {
        String regex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식 입니다.");
        }
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeProfileImage(ProfileImage profileImage){ this.profileImage = profileImage; }

    public void changeRefreshToken(String refreshToken){ this.refreshToken = refreshToken;}

}
