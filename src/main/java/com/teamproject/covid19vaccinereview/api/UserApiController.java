package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;

    @PostMapping("/originlogin")
    public @ResponseBody String originLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest loginRequest){

        Map<String, String> token = userService.login(loginRequest, request.getHeader("Refresh_Token"));
        if(token.get("refreshToken") != null){
            response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        }
        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));


        return "login access";
    }

    @PostMapping("/sociallogin")
    public @ResponseBody String socialLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest loginRequest) throws IOException {

        UserDto findUser = userService.findByGoogleId(loginRequest);

        if(findUser != null){

            Map<String, String> token = userService.login(loginRequest, request.getHeader("Refresh_Token"));
            if(token.get("refreshToken") != null){
                response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
            }
            response.addHeader("Authorization", "Bearer " + token.get("accessToken"));

            return "login Access";

        } else{
            // 구글 로그인 한적이 없으니 분기점( 자체계정이 O -> 로그인 시킴, X -> 회원가입 시킴)으로 보낸다

            return "구글 로그인을 한적 없어서 매핑되지 않음. mappingaccount로 보낸다";
        }
    }

    @PostMapping("/originregister")
    public String originRegister(HttpServletResponse response, @RequestBody JoinRequest joinRequest){

        Map<String, String> token = userService.saveUser(joinRequest);

        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));
        response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        // 예외 핸들러 만들기

        return "join";
    }

    @PostMapping("/mappingaccount")
    public JoinRequest accountMapping(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest loginRequest) throws IOException {

        // 구글로 최초 로그인 시
        UserDto findUser = userService.findByEmail(loginRequest);
        if(findUser == null){

            // 회원가입하고 로그인하세요
            response.sendRedirect("/originregister");

            return JoinRequest.builder()
                    .googleId(loginRequest.getGoogleId()) // GoogleID를 넘겨줘서 회원가입 페이지에서 바로 가입하고 연동까지
                    .build();
        } else{

            Map<String, String> token = userService.mappingAcoount(loginRequest, request.getHeader("Refresh_Token"));
            if(token.get("refreshToken") != null){
                response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
            }
            response.addHeader("Authorization", "Bearer " + token.get("accessToken"));

            return null;
        }
    }

}
