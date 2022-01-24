package com.teamproject.covid19vaccinereview.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImageDto {
    private MultipartFile multipartFile;

    private String fileName;

    private Long fileSize;

    private String fileExtension;

    @Builder
    public ProfileImageDto(MultipartFile multipartFile, String fileName, Long fileSize, String fileExtension) {
        this.multipartFile = multipartFile;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }
}
