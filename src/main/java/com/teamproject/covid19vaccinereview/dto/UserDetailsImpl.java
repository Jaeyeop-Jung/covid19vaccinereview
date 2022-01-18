package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailsImpl implements UserDetails {

    private String email;

    private String password;

    private UserRole role;

    private String nickname;

    private String userPhoto;

    private String googleId;

    private String refreshToken;

    @Builder
    public UserDetailsImpl(String email, String password, UserRole role, String nickname, String userPhoto, String googleId, String refreshToken) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.userPhoto = userPhoto;
        this.googleId = googleId;
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return getRole().toString();
            }
        });

        return grantedAuthority;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){ return password;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
