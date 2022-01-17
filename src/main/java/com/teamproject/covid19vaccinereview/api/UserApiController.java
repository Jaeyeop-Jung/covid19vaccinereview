package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.UserDetailsImpl;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.service.UserDetailsServiceImpl;
import com.teamproject.covid19vaccinereview.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.LogManager;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/originlogin")
    public @ResponseBody String originlogin(@RequestBody UserDto userDto){

        UserDetailsImpl findUserDetails = userDetailsService.loadUserByUsername(userDto.getEmail());

        if(findUserDetails == null) {
            return "Failed to Login";
        }

        return findUserDetails.getRole().toString();
    }

    @PostMapping("/googlelogin")
    public @ResponseBody String googlelogin(@RequestBody UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response){

        UserDetailsImpl findUserDetails = userDetailsService.loadUserByUsername(userDetails.getEmail());
        if(findUserDetails == null){


            return "";
        }
        else {


            return "";
        }
        // 비회원 구글 최초 로그인 시에는 originjoin으로(Header에 구글 id를 들고) 다시 가게해서 회원가입 시킨다
    }

    @PostMapping("/originjoin")
    public String originjoin(HttpServletResponse response, @RequestBody UserDto userDto){

        String accessToken = userService.saveUser(userDto);
        // 저장하고 refresh_Token 생성 후 db저장과 accesstoken 발급
        response.addHeader("Authorization", "Bearer " + accessToken);

        return "join";
    }

    @PostMapping("/googlejoin")
    public @ResponseBody String googlejoin(@RequestBody UserDto userDto){

        // 구글로 최초 로그인 시

        return "";
    }
}
