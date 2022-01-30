package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.EmailDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.NicknameDuplicateException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.NotDefineLoginProviderException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.UserNotFoundException;
import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import com.teamproject.covid19vaccinereview.utils.ProfileImageUtil;
import com.teamproject.covid19vaccinereview.service.Oauth.SocialOauth;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfileImageUtil profileImageUtil;
    private final List<SocialOauth> socialOauthList;

    @Transactional
    public SocialOauth findSocialOauthByLoginProvider(LoginProvider loginProvider){
        return socialOauthList.stream()
                .filter(x -> x.type() == loginProvider)
                .findFirst()
                .orElseThrow( () -> new NotDefineLoginProviderException("알 수 없는 LoginProvider 입니다."));
    }

    /**
     * methodName : login
     * author : Jaeyeop Jung
     * description : ORIGINAL 계정의 로그인 Service.
     *               RefreshToken의 유무에 따라 로직을 분리
     *
     * @param loginRequest     ORIGINAL 로그인에 필요한 정보
     * @param userRefreshToken Header에 담겨온 RefreshToken 정보
     * @return accessToken, refreshToken
     */
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

            if(userRepository.findByEmail(loginRequest.getEmail()).isEmpty() ||
               !bCryptPasswordEncoder.matches(loginRequest.getPassword(), userRepository.findByEmail(loginRequest.getEmail()).get(0).getPassword())){

                log.error("OriginLogin Fail : email = {}, password = {}", loginRequest.getEmail(), loginRequest.getPassword());
                throw new UserNotFoundException("아이디 또는 비밀번호가 잘못됬습니다.");
            }

            User findUser = userRepository.findByEmail(loginRequest.getEmail()).get(0);

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);
            String accessToken = jwtTokenProvider.generateAccessToken(findUser);

            findUser.changeRefreshToken(refreshToken);

            token.put("refreshToken", refreshToken);
            token.put("accessToken", accessToken);

            return token;
        }
    }

    /**
     * methodName : saveUser
     * author : Jaeyeop Jung
     * description : 프로필 사진의 유무에 따라 로직을 분리하고 토큰을 발급한다.
     *
     * @param joinRequest   회원가입에 필요한 정봉
     * @param multipartFile 회원 프로필 이미지 파일
     * @return accessToken, refreshToken
     * @throws IOException the io exception
     */
    @Transactional
    public Map<String, String> saveUser(JoinRequest joinRequest, MultipartFile multipartFile) throws IOException {

        User savedUser;
        if(multipartFile == null || multipartFile.isEmpty()){    // 프로필 이미지 없는 User 저장

            User user = User.of(
                    joinRequest.getEmail(),
                    bCryptPasswordEncoder.encode(joinRequest.getPassword()),
                    UserRole.ROLE_USER,
                    joinRequest.getLoginProvider(),
                    joinRequest.getNickname(),
                    null,
                    null
            );

            if(!userRepository.findByEmail(joinRequest.getEmail()).isEmpty()){
                throw new EmailDuplicateException("중복된 이메일이 존재");
            } else if(!userRepository.findByEmail(joinRequest.getNickname()).isEmpty()){
                throw new NicknameDuplicateException("중복된 닉네임이 존재");
            }

            savedUser = userRepository.save(user);

        } else {                        // 프로필 이미지 있는 User 저장

            joinRequest.initJoinRequest(multipartFile);
            ProfileImage profileImage = ProfileImage.of(
                    joinRequest.getProfileImageDto().getFileName(),
                    joinRequest.getProfileImageDto().getFileSize(),
                    joinRequest.getProfileImageDto().getFileExtension()
            );

            if(!profileImageRepository.findByFileName(profileImage.getFileName()).isEmpty()){
                throw new EmailDuplicateException("중복된 이메일이 존재");
            }
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

            if(!userRepository.findByEmail(joinRequest.getEmail()).isEmpty()){
                throw new EmailDuplicateException("중복된 이메일이 존재");
            } else if(!userRepository.findByEmail(joinRequest.getNickname()).isEmpty()){
                throw new NicknameDuplicateException("중복된 닉네임이 존재");
            }

            savedUser = userRepository.save(user);

            profileImageUtil.saveProfileImage(multipartFile, profileImage.getFileName());

        }

        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);
        String accessToken = jwtTokenProvider.generateAccessToken(savedUser);

        savedUser.changeRefreshToken(refreshToken);

        Map<String, String> token = new HashMap<>();
        token.put("refreshToken", refreshToken);
        token.put("accessToken", accessToken);

        return token;
    }

    /**
     * methodName : oauthLogin
     * author : Jaeyeop Jung
     * description : 회원가입의 유무에 따라 로직을 분리하고 비회원이라면 회원가입 후 토큰을 발급한다.
     *
     * @param loginProvider     the login provider
     * @param authorizationCode the authorization code
     * @return accessToken, refreshToken
     * @throws IOException the io exception
     */
    @Transactional
    public Map<String, String> oauthLogin(LoginProvider loginProvider, String authorizationCode) throws IOException {

        SocialOauth socialOauth = findSocialOauthByLoginProvider(loginProvider); // Oauth 로그인 제공자 찾기

        String oauthAccessToken = socialOauth.requestToken(authorizationCode); // 인가code -> AccessToken
        MultiValueMap<String, Object> userInfo = socialOauth.requestUserInfo(oauthAccessToken);

        List<User> findUserList = userRepository.findByEmail(userInfo.get("email").get(0).toString());

        if( findUserList.isEmpty() ){ // 비회원은 회원가입을 시키고 토큰 반환
            JoinRequest joinRequest = JoinRequest.builder()
                    .email(userInfo.get("email").get(0).toString())
                    .password(bCryptPasswordEncoder.encode(userInfo.get("email").get(0).toString()))
                    .loginProvider((LoginProvider) userInfo.get("loginProvider").get(0))
                    .nickname(userInfo.get("nickname").get(0).toString())
                    .build();
            MultipartFile multipartFile = (MultipartFile) userInfo.get("profileImage").get(0);

            Map<String, String> token = saveUser(joinRequest, multipartFile);

            return token;

        } else {    // 회원은 바로 토큰을 반환한다.
            User findUser = findUserList.get(0);

            Map<String, String> token = new HashMap<>();

            String accessToken = jwtTokenProvider.generateAccessToken(findUser);
            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);

            token.put("accessToken", accessToken);
            token.put("refreshToken", refreshToken);

            return token;
        }
    }

}