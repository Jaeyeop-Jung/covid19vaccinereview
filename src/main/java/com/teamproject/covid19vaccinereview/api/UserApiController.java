package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ParameterBindingException;
import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.LoginResponse;
import com.teamproject.covid19vaccinereview.dto.ModifyUserRequest;
import com.teamproject.covid19vaccinereview.service.UserService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserApiController {

    @Value("${domain-url}")
    private String domainUrl;

    private final UserService userService;

    public void checkParameterBindingException(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            bindingResult.getFieldErrors().forEach(
                    error -> stringBuilder.append(error.getField() + " ")
            );
            throw new ParameterBindingException(stringBuilder.toString());
        }
    }

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
        checkParameterBindingException(bindingResult);

//        HttpHeaders responseHeader = new HttpHeaders();
//        Map<String, String> responseBody = new HashMap<>();

        LoginResponse loginResponse = userService.login(loginRequest, request.getHeader("Refresh_Token"));
//        if(serviceResponse.get("refreshToken") != null){
//            responseHeader.add("Refresh_Token", "Bearer " + serviceResponse.get("refreshToken"));
//        }
//        responseHeader.add("Authorization", "Bearer " + serviceResponse.get("accessToken"));
//
//        if(serviceResponse.get("profileimage") != null){
//            responseBody.put("profileimage", domainUrl + "/profileimage/" + serviceResponse.get("profileimage").toString());
//        } else {
//            responseBody.put("profileimage", null);
//        }
//        responseBody.put("nickname", serviceResponse.get("nickname").toString());

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
    public ResponseEntity<Map<String, String>> originJoin(
            @ModelAttribute @Valid JoinRequest joinRequest,
            BindingResult bindingResult,
            @RequestPart(required = false) @Nullable MultipartFile multipartFile) throws IOException {
        checkParameterBindingException(bindingResult);

        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, String> responseBody = new HashMap<>();

        Map<String, Object> serviceResponse = userService.saveUser(joinRequest, multipartFile);

        if(serviceResponse.get("refreshToken") != null){
            responseHeader.add("Refresh_Token", "Bearer " + serviceResponse.get("refreshToken"));
        }
        responseHeader.add("Authorization", "Bearer " + serviceResponse.get("accessToken"));

        if(serviceResponse.get("profileimage") != null){
            responseBody.put("profileimage", domainUrl + "/profileimage/" + serviceResponse.get("profileimage").toString());
        } else {
            responseBody.put("profileimage", null);
        }
        responseBody.put("nickname", serviceResponse.get("nickname").toString());

        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
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
    public ResponseEntity<Map<String, String>> callback(
            HttpServletResponse response,
            @PathVariable(name = "loginProvider") LoginProvider loginProvider,
            @RequestParam(name = "code") String authorizationCode) throws IOException {

        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, String> responseBody = new HashMap<>();

        log.info("API 서버로부터 받은 code : {}, {}", authorizationCode, loginProvider);
        Map<String, Object> serviceResponse = userService.oauthLogin(loginProvider, authorizationCode);

        if(serviceResponse.get("refreshToken") != null){
            responseHeader.add("Refresh_Token", "Bearer " + serviceResponse.get("refreshToken"));
        }
        responseHeader.add("Authorization", "Bearer " + serviceResponse.get("accessToken"));

        if(serviceResponse.get("profileimage") != null){
            responseBody.put("profileimage", domainUrl + "/profileimage/" + serviceResponse.get("profileimage").toString());
        } else {
            responseBody.put("profileimage", null);
        }
        responseBody.put("nickname", serviceResponse.get("nickname").toString());

        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

    @ApiOperation(value = "회원정보 수정", notes = "회원정보를 수정한다. 헤더에 원하는 계정 토큰을 꼭 담아주세요. wantToChangeProfileImage를 꼭 넣어주세요.")
    @PutMapping("/user")
    public ResponseEntity<Map<String, String>> modifyUser(
            HttpServletRequest request,
            @RequestPart(required = false) @Nullable MultipartFile multipartFile,
            @ModelAttribute ModifyUserRequest modifyUserRequest,
            BindingResult bindingResult
            ) throws IOException {
        checkParameterBindingException(bindingResult);

        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, String> responseBody = new HashMap<>();

        String accessToken = request.getHeader("Authorization").split(" ")[1];
        Map<String, Object> serviceResponse = userService.modify(accessToken, multipartFile, modifyUserRequest);

        if(serviceResponse.get("profileimage") == null){
            responseBody.put("profileimage", null);
        } else {
            responseBody.put("profileimage", domainUrl + "/profileimage/" + serviceResponse.get("profileimage").toString());
        }
        responseBody.put("nickname", serviceResponse.get("nickname").toString());

        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

    @ApiOperation(value = "회원정보 삭제", notes = "회원정보를 삭제한다. 헤더에 원하는 계정 토큰을 꼭 담아주세요.")
    @DeleteMapping("/user")
    public ResponseEntity<Map<String, String>> deleteUser(HttpServletRequest request){

        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, String> responseBody = new HashMap<>();

        String accessToken = request.getHeader("Authorization").split(" ")[1];
        String deletedEmail = userService.delete(accessToken);

        responseBody.put("email", deletedEmail);

        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

}
