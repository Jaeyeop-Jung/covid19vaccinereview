package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWriteRequestDto {

    private String title;

    private String content;

    private String ordinalNumber;

    private VaccineType vaccineType;

    private List<ImageDto> attachedImage;

    @Builder
    public PostWriteRequestDto(String title, String content, String ordinalNumber, VaccineType vaccineType) {
        this.title = title;
        this.content = content;
        this.ordinalNumber = ordinalNumber;
        this.vaccineType = vaccineType;
    }

    public void initPostWriteRequestDto(Map<String, MultipartFile> multipartFile) {
        for (String key : multipartFile.keySet()) {
            MultipartFile getMultipartFile = multipartFile.get(key);
            String fileExtension = getMultipartFile.getOriginalFilename().substring( getMultipartFile.getOriginalFilename().lastIndexOf(".") );
            attachedImage.add(
                    ImageDto.builder()
                            .multipartFile(getMultipartFile)
                            .fileName(getMultipartFile.getOriginalFilename())
                            .fileSize(getMultipartFile.getSize())
                            .fileExtension(fileExtension)
                            .build()
            );
        }
    }
}
