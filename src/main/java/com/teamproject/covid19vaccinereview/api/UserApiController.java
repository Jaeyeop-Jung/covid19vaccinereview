package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.dto.*;
import com.teamproject.covid19vaccinereview.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.teamproject.covid19vaccinereview.utils.BindingParameterUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final BindingParameterUtil bindingParameterUtil;

    /**
     * methodName : originLogin
     * author : Jaeyeop Jung
     * description : ORIGINAL 계정에 로그인하고, 토큰을 발급한다.
     *
     * @param request      the HTTP request
     * @param loginRequest ORIGINAL 로그인에 필요한 정보
     * @return response entity
     */
    @ApiOperation(value = "ORIGINAL 계정 로그인", notes = "ORIGINAL 계정 로그인을 통해 토큰 발급. loginProvider는 정해진 문자열만 입력 바랍니다.")
    @GetMapping("/login")
    public ResponseEntity<LoginResponse> originLogin(HttpServletRequest request, @ModelAttribute @Valid LoginRequest loginRequest, BindingResult bindingResult){
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        LoginResponse loginResponse = userService.login(request, loginRequest);

        return ResponseEntity.ok(loginResponse);
    }


    /**
     * methodName : originJoin
     * author : Jaeyeop Jung
     * description : ORIGINAL 계정을 가입시키고, 토큰을 발급한다.
     *
     * @param joinRequest   ORIGINAL 회원가입에 필요한 정보
     * @param multipartFile 회원 프로필 이미지 파일
     * @param bindingResult the binding result
     * @return response entity
     * @throws IOException the io exception
     */
    @ApiOperation(value = "ORIGINAL 계정 회원가입", notes = "ORIGINAL 계정 회원가입을 통해 토큰 발급. loginProvider는 정해진 문자열만 입력 바랍니다.")
    @PostMapping("/user")
    public ResponseEntity<LoginResponse> originJoin(
            @ModelAttribute @Valid JoinRequest joinRequest,
            BindingResult bindingResult,
            @RequestPart(required = false) @Nullable MultipartFile multipartFile) throws IOException {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        LoginResponse loginResponse = userService.saveUser(joinRequest, multipartFile);

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * methodName : callback
     * author : Jaeyeop Jung
     * description : Oauth 로그인을 통해 토큰을 발급한다.
     * (비회원이면 회원가입을 진행하고 토큰을 발급)
     *
     * @param response          the response
     * @param loginProvider     the login provider
     * @param authorizationCode Oauth 인증에 필요한 1회성 인가코드
     * @return response entity
     * @throws IOException the io exception
     */
    @ApiOperation(value = "Oauth 계정 로그인/회원가입", notes = "Oauth 계정 로그인 시도 / 계정이 없다면 회원가입 후 토큰 발급")
    @GetMapping("/login/{loginProvider}/callback")
    public ResponseEntity<LoginResponse> callback(
            HttpServletResponse response,
            @PathVariable(name = "loginProvider") LoginProvider loginProvider,
            @RequestParam(name = "code") String authorizationCode) throws IOException {

        log.info("API 서버로부터 받은 code : {}, {}", authorizationCode, loginProvider);
        LoginResponse loginResponse = userService.oauthLogin(loginProvider, authorizationCode);

        return ResponseEntity.ok(loginResponse);
    }

    @ApiOperation(value = "회원정보 수정", notes = "회원정보를 수정한다. 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...). wantToChangeProfileImage를 꼭 넣어주세요.")
    @PatchMapping("/user")
    public ResponseEntity<ModifyUserResponse> modifyUser(
            HttpServletRequest request,
            @RequestPart(required = false) @Nullable MultipartFile multipartFile,
            @ModelAttribute ModifyUserRequest modifyUserRequest,
            BindingResult bindingResult
            ) throws IOException {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        ModifyUserResponse modifyUserResponse = userService.modify(request, multipartFile, modifyUserRequest);

        return ResponseEntity.ok(modifyUserResponse);
    }

    @ApiOperation(value = "회원정보 삭제", notes = "회원정보를 삭제한다. 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @DeleteMapping("/user")
    public ResponseEntity<Map<String, String>> deleteUser(
            HttpServletRequest request
    ){

        Map<String, String> response = new HashMap<>();

        String deletedEmail = userService.delete(request);
        response.put("email", deletedEmail);

        return ResponseEntity.ok(response);
    }

}
