package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.service.UserService;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class UserApiController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserApiController(UserService userService,
        UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

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
     * orignjoin
     */
    @PostMapping("/register")
    public String originRegister(HttpServletResponse response,
                                 @RequestParam JoinRequest joinRequest,
                                 @RequestPart MultipartFile multipartFile) throws IOException {
        Map<String, String> token = userService.saveUser(joinRequest, multipartFile);

        response.addHeader("Authorization", "Bearer " + token.get("accessToken"));
        response.addHeader("Refresh_Token", "Bearer " + token.get("refreshToken"));
        // 예외 핸들러 만들기

        return "join";
    }

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

    @GetMapping(value = "/test")
    public ResponseEntity<UserDto> testUser() {
        UserDto userResponse = UserDto.of("email", "password", UserRole.ROLE_ADMIN, "nickname", "googleId", "refreshToken");
        return ResponseEntity.ok().body(userResponse);
    }

}
