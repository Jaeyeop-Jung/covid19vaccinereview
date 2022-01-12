package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.UserDetailsImpl;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.service.UserDetailsServiceImpl;
import com.teamproject.covid19vaccinereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/join")
    public @ResponseBody String join(@RequestBody UserDto userDto){

        userService.saveUser(userDto);


        return "join";
    }

    @PostMapping("/loginForm")
    public @ResponseBody String login(@RequestBody UserDto userDto){

        System.out.println("userDto = " + userDto);

        UserDetailsImpl findUserDetails = userDetailsService.loadUserByUsername(userDto.getEmail());

        if(findUserDetails == null) {
            return "Failed to Login";
        }

        return findUserDetails.getRole().toString();
    }
}
