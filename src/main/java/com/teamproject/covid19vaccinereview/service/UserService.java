package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserDto findByEmail(LoginRequest loginRequest){
        try {
            User findUser = userRepository.findByEmail(loginRequest.getEmail()).get(0);

            return UserDto.builder()
                    .email(findUser.getEmail())
                    .password(findUser.getPassword())
                    .role(findUser.getRole())
                    .nickname(findUser.getNickname())
                    .googleId(findUser.getGoogleId())
                    .refreshToken(findUser.getRefreshToken())
                    .build();
        }
        catch (Exception e){
            return null;
        }
    }

    @Transactional
    public Map<String, String> login(LoginRequest loginRequest, String userRefreshToken){

        requestLoginApi(loginRequest.getAuthorizationCode());



        Map<String, String> token = new HashMap<>();

        if(jwtTokenProvider.validateToken(userRefreshToken)){
            String userId = jwtTokenProvider.findUserIdByJwt(userRefreshToken);
            User findUser = userRepository.findById(Long.parseLong(userId)).get();

            String accessToken = jwtTokenProvider.generateAccessToken(findUser);
            token.put("accessToken", accessToken);

            return token;
        } else{
            User findUser = userRepository.findByEmail(loginRequest.getEmail()).get(0);

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);
            String accessToken = jwtTokenProvider.generateAccessToken(findUser);

            findUser.changeRefreshToken(refreshToken);

            token.put("refreshToken", refreshToken);
            token.put("accessToken", accessToken);

            return token;
        }
    }

    @Transactional
    public Map<String, String> saveUser(JoinRequest joinRequest, MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()){
            throw new IOException("MultipartFile이 제대로 넘어오지 않았습니다");
        }

        joinRequest.initJoinRequest(multipartFile);
        ProfileImage profileImage = ProfileImage.of(
                joinRequest.getProfileImageDto().getFileName(),
                joinRequest.getProfileImageDto().getFileSize(),
                joinRequest.getProfileImageDto().getFileExtension()
        );
        profileImageRepository.save(profileImage);

        User user = User.of(
                joinRequest.getEmail(),
                bCryptPasswordEncoder.encode(joinRequest.getPassword()),
                UserRole.ROLE_USER,
                joinRequest.getProvider(),
                joinRequest.getNickname(),
                profileImage,
                null
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
    public void requestLoginApi(String authorizationCode){



    }
}