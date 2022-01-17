package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String saveUser(UserDto userDto){
        User user = UserDto.toEntity(
                userDto.getEmail(),
                bCryptPasswordEncoder.encode(userDto.getPassword()),
                userDto.getNickname(),
                userDto.getUserPhoto(),
                userDto.getGoogleId(),
                userDto.getRefreshToken()
        );
        User savedUser = userRepository.save(user);

        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);
        String accessToken = jwtTokenProvider.generateAccessToken(savedUser);

        savedUser.changeRefreshToken(refreshToken);

        return accessToken;
    }

}