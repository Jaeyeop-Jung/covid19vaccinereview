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

    private ImageDto imageDto;


    /**
     * methodName : initJoinRequest
     * author : Jaeyeop Jung
     * description : FE에서 넘어온 MultipartFile을 통해 imageDto 초기화한다.
     *
     * @param multipartFile 회원 프로필 이미지 파일
     */
    public void initJoinRequest(MultipartFile multipartFile){

        String fileExtension = multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf(".") );
        this.imageDto = ImageDto.builder()
                .multipartFile(multipartFile)
                .fileName(getEmail() + fileExtension)
                .fileSize(multipartFile.getSize())
                .fileExtension(fileExtension)
                .build();
    }

    @Builder
    public JoinRequest(String email, String password, LoginProvider loginProvider, String nickname, ImageDto imageDto) {
        this.email = email;
        this.password = password;
        this.loginProvider = loginProvider;
        this.nickname = nickname;
        this.imageDto = imageDto;
    }
}
