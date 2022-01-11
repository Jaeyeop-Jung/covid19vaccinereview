package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDtoDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {  // 원래는 UserDetails을 반환형으로 하지만 자식 객체인 UserDto를 반환하게함 ( UserDto의 필드들을 사용하기 위해)

        List<User> findUser = userRepository.findByEmail(email);

        if(findUser != null){
            return UserDto.builder()
                    .email(findUser.get(0).getEmail())
                    .password(findUser.get(0).getPassword())
                    .role(findUser.get(0).getRole())
                    .nickname(findUser.get(0).getNickname())
                    .userPhoto(findUser.get(0).getUserPhoto())
                    .build();
        }

        return null;
    }
}
