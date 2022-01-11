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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserDto implements UserDetails {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private UserRole role;

    @NotNull
    private String nickname;

    private String userPhoto;

    public static User toEntity(String email, String password, UserRole role, String nickname, String userPhoto){
        return User.of(email, password, role, nickname, userPhoto);
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
        return getEmail();
    }

    @Override
    public String getPassword(){ return getPassword();}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
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
