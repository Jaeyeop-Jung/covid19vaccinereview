package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserDto findByEmail(UserDto userDto){
        try {
            User findUser = userRepository.findByEmail(userDto.getEmail()).get(0);

            return UserDto.builder()
                    .email(findUser.getEmail())
                    .password(findUser.getPassword())
                    .role(findUser.getRole())
                    .nickname(findUser.getNickname())
                    .userPhoto(findUser.getUserPhoto())
                    .googleId(findUser.getGoogleId())
                    .refreshToken(findUser.getRefreshToken())
                    .build();
        }
        catch (Exception e){
            return null;
        }
    }

    @Transactional
    public UserDto findByGoogleId(UserDto userDto){
        try {
            User findUser = userRepository.findByGoogleId(userDto.getGoogleId()).get(0);

            return UserDto.builder()
                    .email(findUser.getEmail())
                    .password(findUser.getPassword())
                    .role(findUser.getRole())
                    .nickname(findUser.getNickname())
                    .userPhoto(findUser.getUserPhoto())
                    .googleId(findUser.getGoogleId())
                    .refreshToken(findUser.getRefreshToken())
                    .build();
        }
        catch (Exception e){
            return null;
        }
    }

    @Transactional
    public Map<String, String> login(UserDto userDto, String userRefreshToken){

        Map<String, String> token = new HashMap<>();

        if(jwtTokenProvider.validateToken(userRefreshToken)){
            String userId = jwtTokenProvider.findUserIdByJwt(userRefreshToken);
            User findUser = userRepository.findById(Long.parseLong(userId)).get();

            String accessToken = jwtTokenProvider.generateAccessToken(findUser);
            token.put("accessToken", accessToken);

            return token;
        } else{
            User findUser = userRepository.findByEmail(userDto.getEmail()).get(0);

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);
            String accessToken = jwtTokenProvider.generateAccessToken(findUser);

            findUser.changeRefreshToken(refreshToken);

            token.put("refreshToken", refreshToken);
            token.put("accessToken", accessToken);

            return token;
        }
    }

    @Transactional
    public Map<String, String> saveUser(UserDto userDto){
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

        Map<String, String> token = new HashMap<>();
        token.put("refreshToken", refreshToken);
        token.put("accessToken", accessToken);

        return token;
    }


    @Transactional
    public Map<String, String> joinGoogle(UserDto userDto, String userRefreshToken){

        Map<String, String> token = new HashMap<>();

        if(jwtTokenProvider.validateToken(userRefreshToken)){

            User findUser = userRepository.findByEmail(userDto.getEmail()).get(0);
            findUser.changeGoogleId(userDto.getGoogleId());

            String accessToken = jwtTokenProvider.generateAccessToken(findUser);
            token.put("accessToken", accessToken);

            return token;
        } else{

            User findUser = userRepository.findByEmail(userDto.getEmail()).get(0);
            findUser.changeGoogleId(userDto.getGoogleId());

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);
            String accessToken = jwtTokenProvider.generateAccessToken(findUser);

            findUser.changeRefreshToken(refreshToken);

            token.put("refreshToken", refreshToken);
            token.put("accessToken", accessToken);

            return token;
        }

    }

}