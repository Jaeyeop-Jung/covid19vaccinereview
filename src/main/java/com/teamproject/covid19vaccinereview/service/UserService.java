package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.*;
import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.*;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import com.teamproject.covid19vaccinereview.utils.ProfileImageUtil;
import com.teamproject.covid19vaccinereview.service.Oauth.SocialOauth;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${domain-url}")
    private String domainUrl;

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfileImageUtil profileImageUtil;
    private final List<SocialOauth> socialOauthList;

    @Transactional(readOnly = true)
    public User getLoginUserByAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new MalformedJwtException("");
        }

        return userRepository.findById(jwtTokenProvider.findUserIdByJwt(accessToken))
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    @Transactional(readOnly = true)
    public User getLoginUserByRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");

        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new MalformedJwtException("");
        }

        return userRepository.findById(jwtTokenProvider.findUserIdByJwt(refreshToken))
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    @Transactional(readOnly = true)
    public SocialOauth findSocialOauthByLoginProvider(LoginProvider loginProvider){
        return socialOauthList.stream()
                .filter(x -> x.type() == loginProvider)
                .findFirst()
                .orElseThrow( () -> new NotDefineLoginProviderException("알 수 없는 LoginProvider 입니다."));
    }

    @Transactional(readOnly = true)
    public LoginResponse createLoginResponse(User user, String refreshToken){

        ProfileImage profileImage = user.getProfileImage();
        if(profileImage != null){
            return LoginResponse.builder()
                    .accessToken("Bearer " + jwtTokenProvider.generateAccessToken(user))
                    .refreshToken(refreshToken)
                    .nickname(user.getNickname())
                    .profileImageUrl(domainUrl + "/profileimage/" + profileImage.getId())
                    .build();
        } else {
            return LoginResponse.builder()
                    .accessToken("Bearer " + jwtTokenProvider.generateAccessToken(user))
                    .refreshToken(refreshToken)
                    .nickname(user.getNickname())
                    .build();
        }
    }

    /**
     * methodName : login
     * author : Jaeyeop Jung
     * description : ORIGINAL 계정의 로그인 Service.
     * RefreshToken의 유무에 따라 로직을 분리
     *
     * @param request      HttpRequest
     * @param loginRequest ORIGINAL 로그인에 필요한 정보
     * @return LoginResponse
     */
    @Transactional
    public LoginResponse login(HttpServletRequest request, LoginRequest loginRequest){

        if(request.getHeader("refreshToken") != null){
            User findUser = getLoginUserByRefreshToken(request);

            return createLoginResponse(findUser, null);

        } else{

            User findUser = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("아이디 또는 비밀번호가 잘못됬습니다."));
            if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())){
                throw new UserNotFoundException("아이디 또는 비밀번호가 잘못됬습니다.");
            }

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);
            findUser.changeRefreshToken(refreshToken);

            return createLoginResponse(findUser, refreshToken);
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
    public LoginResponse saveUser(JoinRequest joinRequest, MultipartFile multipartFile) throws IOException {

        if(userRepository.existsByEmail(joinRequest.getEmail())){
            throw new EmailDuplicateException("중복된 이메일이 존재");
        } else if(userRepository.existsByNickname(joinRequest.getNickname())){
            throw new NicknameDuplicateException("중복된 닉네임이 존재");
        }

        User savedUser;
        if(multipartFile == null || multipartFile.isEmpty()){    // 프로필 이미지 없는 User 저장

            savedUser = userRepository.save(
                    User.of(
                            joinRequest.getEmail(),
                            bCryptPasswordEncoder.encode(joinRequest.getPassword()),
                            UserRole.ROLE_USER,
                            joinRequest.getLoginProvider(),
                            joinRequest.getNickname(),
                            null,
                            null
                    )
            );

        } else {                        // 프로필 이미지 있는 User 저장

            joinRequest.initJoinRequest(multipartFile);
            ProfileImage profileImage = ProfileImage.of(
                    joinRequest.getImageDto().getFileName(),
                    joinRequest.getImageDto().getFileSize(),
                    joinRequest.getImageDto().getFileExtension()
            );
            profileImageRepository.save(profileImage);

            savedUser = userRepository.save(
                    User.of(
                            joinRequest.getEmail(),
                            bCryptPasswordEncoder.encode(joinRequest.getPassword()),
                            UserRole.ROLE_USER,
                            joinRequest.getLoginProvider(),
                            joinRequest.getNickname(),
                            profileImage,
                            null
                    )
            );

            profileImageUtil.saveProfileImage(multipartFile, profileImage.getFileName());
        }

        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);
        savedUser.changeRefreshToken(refreshToken);

        return createLoginResponse(savedUser, refreshToken);
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
    public LoginResponse oauthLogin(LoginProvider loginProvider, String authorizationCode) throws IOException {

        SocialOauth socialOauth = findSocialOauthByLoginProvider(loginProvider); // Oauth 로그인 제공자 찾기

        String oauthAccessToken = socialOauth.requestToken(authorizationCode); // 인가code -> AccessToken
        MultiValueMap<String, Object> userInfo = socialOauth.requestUserInfo(oauthAccessToken);

        Optional<User> findUser = userRepository.findByEmail(userInfo.get("email").get(0).toString());

        if( findUser.isPresent() ){ // 회원은 바로 토큰 반환
            Map<String, Object> response = new HashMap<>();

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser.get());
            findUser.get().changeRefreshToken(refreshToken);

            return createLoginResponse(findUser.get(), refreshToken);

        } else {    // 비회원은 회원가입을 하고 토큰을 반환한다.

            JoinRequest joinRequest = JoinRequest.builder()
                    .email(userInfo.get("email").get(0).toString())
                    .password(bCryptPasswordEncoder.encode(userInfo.get("email").get(0).toString()))
                    .loginProvider((LoginProvider) userInfo.get("loginProvider").get(0))
                    .nickname(userInfo.get("nickname").get(0).toString())
                    .build();

            LoginResponse loginResponse = saveUser(joinRequest, (MultipartFile) userInfo.get("profileImage").get(0));

            return loginResponse;
        }

    }

    @Transactional
    public UserModifyResponse modify(HttpServletRequest request, MultipartFile multipartFile, UserModifyRequest userModifyRequest) throws IOException {

        Map<String, Object> response = new HashMap<>();

        if(!jwtTokenProvider.validateToken(request.getHeader("Authorization").split(" ")[1])){
            throw new MalformedJwtException("");
        }

        User findUser = getLoginUserByAccessToken(request);

        if(userModifyRequest.isWantToChangeProfileImage() && !multipartFile.isEmpty()){ // 프로필 이미지 수정

            String fileExtension = multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf(".") );

            if(findUser.getProfileImage() == null){

                ProfileImage profileImage = ProfileImage.of(
                        findUser.getEmail() + fileExtension,
                        multipartFile.getSize(),
                        fileExtension
                );
                profileImageRepository.save(profileImage);
                findUser.changeProfileImage(profileImage);

                profileImageUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
                profileImageUtil.saveProfileImage(multipartFile, findUser.getProfileImage().getFileName());

            } else {

                findUser.getProfileImage().changeImage(
                        findUser.getEmail() + fileExtension,
                        multipartFile.getSize(),
                        fileExtension
                );

                profileImageUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
                profileImageUtil.saveProfileImage(multipartFile, findUser.getProfileImage().getFileName());
            }

        } else if(userModifyRequest.isWantToChangeProfileImage() && multipartFile.isEmpty()) {

            if(findUser.getProfileImage() != null){
                profileImageUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
                profileImageRepository.deleteById(findUser.getProfileImage().getId());
            }

        }

        if(userModifyRequest.getPassword() != null && userModifyRequest.getPassword().length() != 0){   // 비밀번호 수정

            if(userModifyRequest.getPassword().isBlank()){
                throw new BlankPasswordException("");
            } else if(bCryptPasswordEncoder.matches(userModifyRequest.getPassword(), findUser.getPassword())){
                throw new SamePasswordException("");
            }

            findUser.changePassword(bCryptPasswordEncoder.encode(userModifyRequest.getPassword()));
        }

        if(userModifyRequest.getNickname() != null && userModifyRequest.getNickname().length() != 0){   // 닉네임 수정

            if(userModifyRequest.getNickname().isBlank()){
                throw new BlankNicknameException("");
            }

            if(userRepository.findByNickname(userModifyRequest.getNickname()).isEmpty()){
                findUser.changeNickname(userModifyRequest.getNickname());
            } else {
                throw new NicknameDuplicateException("");
            }
        }

        if(findUser.getProfileImage() != null){
            return UserModifyResponse.builder()
                    .nickname(findUser.getNickname())
                    .profileImageUrl(domainUrl + "/profileimage/" + findUser.getProfileImage().getId())
                    .build();

        } else {
            return UserModifyResponse.builder()
                    .nickname(findUser.getNickname())
                    .build();
        }
    }

    @Transactional
    public String delete(HttpServletRequest request){

        User findUser = getLoginUserByAccessToken(request);

//        if(!userRepository.existsById(userId)){
//            throw new IncorrectDeleteUserRequestException("");
//        }

//        Optional<User> findUser = userRepository.findById(userId);
//        String email = findUser.get().getEmail();

        if(findUser.getProfileImage() !=  null){
            profileImageUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
            profileImageRepository.deleteById(findUser.getProfileImage().getId());
        }
        userRepository.delete(findUser);

        return findUser.getEmail();
    }

}