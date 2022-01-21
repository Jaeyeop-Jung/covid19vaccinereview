package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import com.teamproject.covid19vaccinereview.service.Oauth.SocialOauth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final List<SocialOauth> socialOauthList;

    @Transactional
    public SocialOauth findSocialOauthByLoginProvider(LoginProvider loginProvider){
        return socialOauthList.stream()
                .filter(x -> x.type() == loginProvider)
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException("알 수 없는 LoginProvider 입니다."));
    }

    @Transactional
    public Map<String, String> login(LoginRequest loginRequest, String userRefreshToken){

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
                joinRequest.getLoginProvider(),
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
    public String oauthLogin(LoginProvider loginProvider, String authorizationCode){
        String responseBody = requestAccessToken(loginProvider, authorizationCode);

        return responseBody;
    }

    @Transactional
    public String requestAccessToken(LoginProvider loginProvider, String authorizationCode){

        SocialOauth socialOauth = findSocialOauthByLoginProvider(loginProvider);
        return socialOauth.requestAccessToken(authorizationCode);
    }
}