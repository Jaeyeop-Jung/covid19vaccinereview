package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import com.teamproject.covid19vaccinereview.service.UserDtoDetailsService;
import com.teamproject.covid19vaccinereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserDtoDetailsService userDtoDetailsService;

    @PostMapping("/join")
    public @ResponseBody String join(@RequestBody UserDto userDto){

        userService.saveUser(userDto);

        return "join";
    }

    @PostMapping("/loginForm")
    public @ResponseBody String login(@RequestBody UserDto userDto){

        System.out.println("userDto = " + userDto);

        UserDto findUserDto = userDtoDetailsService.loadUserByUsername(userDto.getEmail());

        if(findUserDto == null) {
            return "Failed to Login";
        }

        return findUserDto.getRole().toString();
    }
}
