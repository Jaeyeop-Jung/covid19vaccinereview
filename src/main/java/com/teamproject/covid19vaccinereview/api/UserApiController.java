package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.service.UserService;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    /**
     * methodName : originLogin
     * author : Jaeyeop Jung
     * description : ORIGINAL 계정에 로그인하고, 토큰을 발급한다.
     *
     * @param request      the HTTP request
     * @param response     the HTTP response
     * @param loginRequest LoginRequest : ORIGINAL 로그인에 필요한 정보
     * @return
     */
    @ApiOperation(value = "ORIGINAL 계정 로그인", notes = "ORIGINAL 계정 로그인을 통해 토큰 발급")
    @PostMapping("/login")
    public @ResponseBody String originLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest loginRequest){
        Map<String, String> token = userService.login(loginRequest, request.getHeader("Refresh_Token"));
        if(token.get("refreshToken") != null){
            response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        }
        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));


        return "login success";
    }


    /**
     * methodName : originJoin
     * author : Jaeyeop Jung
     * description : ORIGINAL 계정을 가입시키고, 토큰을 발급한다.
     *
     * @param response      the HTTP response
     * @param joinRequest   JoinRequest : 회원가입에 필요한 정보
     * @param multipartFile MultipartFile : 회원 이미지 파일
     * @return
     * @throws IOException the io exception
     */
    @ApiOperation(value = "ORIGINAL 계정 회원가입", notes = "ORIGINAL 계정 회원가입을 통해 토큰 발급")
    @PostMapping("/join")
    public String originJoin(HttpServletResponse response,
                                 @RequestPart JoinRequest joinRequest,
                                 @RequestPart MultipartFile multipartFile) throws IOException {
        Map<String, String> token = userService.saveUser(joinRequest, multipartFile);

        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));
        response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        // 예외 핸들러 만들기

        return "join";
    }

    @ApiOperation(value = "Oauth 계정 로그인/회원가입", notes = "Oauth 계정 로그인 시도 / 계정이 없다면 회원가입 후 토큰 발급")
    @GetMapping("/login/{loginProvider}/callback")
    public String callback(
            HttpServletResponse response,
            @PathVariable(name = "loginProvider") LoginProvider loginProvider,
            @RequestParam(name = "code") String authorizationCode) throws IOException {

        log.info("API 서버로부터 받은 code : {}, {}", authorizationCode, loginProvider);
        Map<String, String> token = userService.oauthLogin(loginProvider, authorizationCode);

        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));
        response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));

        return "login success";
    }

}
