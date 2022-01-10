package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserDto userDto){
        User user = UserDto.toEntity(
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getNickname(),
                userDto.getUserPhoto()
        );

        userRepository.save(user);
    }


}
