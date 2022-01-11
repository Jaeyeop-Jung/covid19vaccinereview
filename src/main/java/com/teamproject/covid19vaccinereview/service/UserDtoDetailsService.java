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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

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
