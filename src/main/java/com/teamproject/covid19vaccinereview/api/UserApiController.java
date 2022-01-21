package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


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

    @PostMapping("/register")
    public String originRegister(HttpServletResponse response,
                                 @RequestPart JoinRequest joinRequest,
                                 @RequestPart MultipartFile multipartFile) throws IOException {
        Map<String, String> token = userService.saveUser(joinRequest, multipartFile);

        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));
        response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        // 예외 핸들러 만들기

        return "join";
    }

}
