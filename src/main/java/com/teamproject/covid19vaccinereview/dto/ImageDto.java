package com.teamproject.covid19vaccinereview.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDto {

    @ApiParam(hidden = true)
    private MultipartFile multipartFile;

    @ApiParam(hidden = true)
    private String fileName;

    @ApiParam(hidden = true)
    private Long fileSize;

    @ApiParam(hidden = true)
    private String fileExtension;

    @Builder
    public ImageDto(MultipartFile multipartFile, String fileName, Long fileSize, String fileExtension) {
        this.multipartFile = multipartFile;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }
}
