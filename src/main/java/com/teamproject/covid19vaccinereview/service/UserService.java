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
import com.teamproject.covid19vaccinereview.utils.ImageFileUtil;
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
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${domain-url}")
    private String domainUrl;

    private final ProfileImageService profileImageService;

    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageFileUtil imageFileUtil;
    private final List<SocialOauth> socialOauthList;

    @Transactional(readOnly = true)
    public User getLoginUserByAccessToken(HttpServletRequest request) {
        if(request.getHeader("Authorization") == null){
            throw new JwtIllegalArgumentException("");
        }
        if(
                request.getHeader("Authorization").isBlank() ||
                !request.getHeader("Authorization").startsWith("Bearer ")
        ) {
            throw new MalformedJwtException("");
        }

        String accessToken = request.getHeader("Authorization").split(" ")[1];

        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new MalformedJwtException("");
        }

        return userRepository.findById(jwtTokenProvider.findUserIdByJwt(accessToken))
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    @Transactional(readOnly = true)
    public User getLoginUserWithoutExceptionByAccessToken(HttpServletRequest request){

        if(     request.getHeader("Authorization") == null ||
                request.getHeader("Authorization").isBlank() ||
                !request.getHeader("Authorization").startsWith("Bearer ")
        ){
            return null;
        }

        String accessToken = request.getHeader("Authorization").split(" ")[1];

        if(!jwtTokenProvider.validateToken(accessToken)){
            return null;
        } else {
            return userRepository.findById(jwtTokenProvider.findUserIdByJwt(accessToken))
                    .orElse(null);
        }
    }

    @Transactional(readOnly = true)
    public User getLoginUserByRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");

        if(refreshToken == null || refreshToken.isBlank()) {
            return null;
        }

        if(!jwtTokenProvider.validateToken(refreshToken) || userRepository.existsByRefreshToken(refreshToken)){
            throw new MalformedJwtException("refreshToken : " + refreshToken);
        }

        return userRepository.findById(jwtTokenProvider.findUserIdByJwt(refreshToken))
                .orElseThrow(() -> new UserNotFoundException(""));
    }

    @Transactional(readOnly = true)
    public SocialOauth findSocialOauthByLoginProvider(LoginProvider loginProvider){
        return socialOauthList.stream()
                .filter(x -> x.type() == loginProvider)
                .findFirst()
                .orElseThrow( () -> new NotDefineLoginProviderException("??? ??? ?????? LoginProvider ?????????."));
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
     * description : ORIGINAL ????????? ????????? Service.
     * RefreshToken??? ????????? ?????? ????????? ??????
     *
     * @param request      HttpRequest
     * @param loginRequest ORIGINAL ???????????? ????????? ??????
     * @return LoginResponse
     */
    @Transactional
    public LoginResponse login(HttpServletRequest request, LoginRequest loginRequest){

        if( getLoginUserByRefreshToken(request) != null){
            User findUser = getLoginUserByRefreshToken(request);

            return createLoginResponse(findUser, null);

        } else{

            User findUser = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("????????? ?????? ??????????????? ??????????????????."));
            if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())){
                throw new UserNotFoundException("????????? ?????? ??????????????? ??????????????????.");
            }

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser);
            findUser.changeRefreshToken(refreshToken);

            return createLoginResponse(findUser, refreshToken);
        }
    }

    /**
     * methodName : saveUser
     * author : Jaeyeop Jung
     * description : ????????? ????????? ????????? ?????? ????????? ???????????? ????????? ????????????.
     *
     * @param joinRequest   ??????????????? ????????? ??????
     * @param multipartFile ?????? ????????? ????????? ??????
     * @return accessToken, refreshToken
     * @throws IOException the io exception
     */
    @Transactional
    public LoginResponse saveUser(JoinRequest joinRequest, MultipartFile multipartFile) throws IOException {

        if(userRepository.existsByEmail(joinRequest.getEmail())){
            throw new EmailDuplicateException("????????? ???????????? ??????");
        } else if(userRepository.existsByNickname(joinRequest.getNickname())){
            throw new NicknameDuplicateException("????????? ???????????? ??????");
        }

        User savedUser;
        if(multipartFile == null || multipartFile.isEmpty()){    // ????????? ????????? ?????? User ??????

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

        } else {                        // ????????? ????????? ?????? User ??????

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

            imageFileUtil.saveProfileImage(multipartFile, profileImage.getFileName());
        }

        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser);
        savedUser.changeRefreshToken(refreshToken);

        return createLoginResponse(savedUser, refreshToken);
    }

    /**
     * methodName : oauthLogin
     * author : Jaeyeop Jung
     * description : ??????????????? ????????? ?????? ????????? ???????????? ?????????????????? ???????????? ??? ????????? ????????????.
     *
     * @param loginProvider     the login provider
     * @param authorizationCode the authorization code
     * @return accessToken, refreshToken
     * @throws IOException the io exception
     */
    @Transactional
    public LoginResponse oauthLogin(LoginProvider loginProvider, String authorizationCode) throws IOException {

        SocialOauth socialOauth = findSocialOauthByLoginProvider(loginProvider); // Oauth ????????? ????????? ??????

        String oauthAccessToken = socialOauth.requestToken(authorizationCode); // ??????code -> AccessToken
        MultiValueMap<String, Object> userInfo = socialOauth.requestUserInfo(oauthAccessToken);

        Optional<User> findUser = userRepository.findByEmail(userInfo.get("email").get(0).toString());

        if( findUser.isPresent() ){ // ????????? ?????? ?????? ??????

            String refreshToken = jwtTokenProvider.generateRefreshToken(findUser.get());
            findUser.get().changeRefreshToken(refreshToken);

            return createLoginResponse(findUser.get(), refreshToken);

        } else {    // ???????????? ??????????????? ?????? ????????? ????????????.

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
    public ModifyUserResponse modify(HttpServletRequest request, MultipartFile multipartFile, ModifyUserRequest modifyUserRequest) throws IOException {

        if(!jwtTokenProvider.validateToken(request.getHeader("Authorization").split(" ")[1])){
            throw new MalformedJwtException("");
        }

        User findUser = getLoginUserByAccessToken(request);

        profileImageService.modify(findUser, multipartFile, modifyUserRequest); // ????????? ????????? ??????

        if(modifyUserRequest.getPassword() != null && modifyUserRequest.getPassword().length() != 0){   // ???????????? ??????

            if(modifyUserRequest.getPassword().isBlank()){
                throw new BlankPasswordException("");
            } else if(bCryptPasswordEncoder.matches(modifyUserRequest.getPassword(), findUser.getPassword())){
                throw new SamePasswordException("");
            }

            findUser.changePassword(bCryptPasswordEncoder.encode(modifyUserRequest.getPassword()));
        }

        if(modifyUserRequest.getNickname() != null && modifyUserRequest.getNickname().length() != 0){   // ????????? ??????

            if(modifyUserRequest.getNickname().isBlank()){
                throw new BlankNicknameException("");
            }

            if(userRepository.findByNickname(modifyUserRequest.getNickname()).isEmpty()){
                findUser.changeNickname(modifyUserRequest.getNickname());
            } else {
                throw new NicknameDuplicateException("");
            }
        }

        if(findUser.getProfileImage() != null){
            return ModifyUserResponse.builder()
                    .nickname(findUser.getNickname())
                    .profileImageUrl(domainUrl + "/profileimage/" + findUser.getProfileImage().getId())
                    .build();

        } else {
            return ModifyUserResponse.builder()
                    .nickname(findUser.getNickname())
                    .build();
        }
    }

    @Transactional
    public String delete(HttpServletRequest request){

        User findUser = getLoginUserByAccessToken(request);

        if(findUser.getProfileImage() !=  null){
            imageFileUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
            profileImageRepository.deleteById(findUser.getProfileImage().getId());
        }
        userRepository.delete(findUser);

        return findUser.getEmail();
    }

}