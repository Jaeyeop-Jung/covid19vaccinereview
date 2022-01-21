package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.UserProvider;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class JoinRequest {

    private String email;

    private String password;

    private UserProvider provider;

    private String nickname;

    private ProfileImageDto profileImageDto;

    private String googleId;

    public void initJoinRequest(MultipartFile multipartFile){

        String fileExtension = multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf(".") );
        this.profileImageDto = ProfileImageDto.builder()
                .multipartFile(multipartFile)
                .fileName(multipartFile.getOriginalFilename())
                .fileSize(multipartFile.getSize())
                .fileExtension(fileExtension)
                .build();
    }
}
