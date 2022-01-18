package com.teamproject.covid19vaccinereview.api;

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
    public @ResponseBody String originLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDto userDto){

        Map<String, String> token = userService.login(userDto, request.getHeader("Refresh_Token"));
        if(token.get("refreshToken") != null){
            response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        }
        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));


        return "login access";
    }

    @PostMapping("/googlelogin")
    public @ResponseBody String googleLogin(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response){

        UserDto findUser = userService.findByGoogleId(userDto);

        if(findUser != null){

            Map<String, String> token = userService.login(findUser, request.getHeader("Refresh_Token"));
            if(token.get("refreshToken") != null){
                response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
            }
            response.addHeader("Authorization", "Bearer " + token.get("accessToken"));

            return "login Access";

        } else{
            // 구글 로그인 한적이 없으니 googleJoin으로 보낸다

            return "구글 로그인을 한적 없어서 매핑되지 않음. googleJoin으로 보낸다";
        }
    }

    @PostMapping("/originjoin")
    public String originJoin(HttpServletResponse response, @RequestBody UserDto userDto){

        Map<String, String> token = userService.saveUser(userDto);

        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));
        response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        // 예외 핸들러 만들기

        return "join";
    }

    @PostMapping("/googlejoin")
    public @ResponseBody String googleJoin(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDto userDto) throws IOException {

        // 구글로 최초 로그인 시
        UserDto findUser = userService.findByEmail(userDto);
        if(findUser == null){

            // 회원가입하고 다시 돌아오도록

            return "자체 회원가입 하고 오게";
        } else{

            Map<String, String> token = userService.joinGoogle(userDto, request.getHeader("Refresh_Token"));
            if(token.get("refreshToken") != null){
                response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
            }
            response.addHeader("Authorization", "Bearer " + token.get("accessToken"));

            return "구글 로그인 완료";
        }
    }

}
