package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWriteRequest {

    private String title;

    private String content;

    private int ordinalNumber;

    private VaccineType vaccineType;

    private List<ImageDto> attachedImage = new ArrayList<>();

    @Builder
    public PostWriteRequest(String title, String content, int ordinalNumber, VaccineType vaccineType) {
        this.title = title;
        this.content = content;
        this.ordinalNumber = ordinalNumber;
        this.vaccineType = vaccineType;
    }

    public void initPostWriteRequestDto(List<MultipartFile> multipartFileList) {

        for (MultipartFile multipartFile : multipartFileList) {
            String fileExtension = multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf(".") );
            attachedImage.add(
                    ImageDto.builder()
                            .multipartFile(multipartFile)
                            .fileName(multipartFile.getOriginalFilename())
                            .fileSize(multipartFile.getSize())
                            .fileExtension(fileExtension)
                            .build()
            );
        }


    }
}
