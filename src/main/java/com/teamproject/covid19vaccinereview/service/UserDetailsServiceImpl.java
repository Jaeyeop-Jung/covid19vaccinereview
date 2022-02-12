package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.IncorrectDeleteUserRequestException;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDetailsImpl;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * methodName : loadUserByUsername
     * author : Jaeyeop Jung
     * description : Context에 담기 위해 UserDetails를 implement한 UserDetailsImpl을 반환
     *
     * @param id
     * @return id를 통해 찾은 UserDetailsImpl 객체
     */
    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String id) {

        User findUser = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new IncorrectDeleteUserRequestException(""));

        if(findUser != null){
            return UserDetailsImpl.builder()
                    .email(findUser.getEmail())
                    .password(findUser.getPassword())
                    .role(findUser.getRole())
                    .loginProvider(findUser.getLoginProvider())
                    .nickname(findUser.getNickname())
                    .profileImage(findUser.getProfileImage())
                    .refreshToken(findUser.getRefreshToken())
                    .build();
        }

        return null;
    }
}
