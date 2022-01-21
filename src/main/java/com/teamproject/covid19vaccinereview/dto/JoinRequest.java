package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class JoinRequest {

    private String email;

    private String password;

    private LoginProvider loginProvider;

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