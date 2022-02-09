package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyPostRequest {

    @NotNull
    @ApiParam(required = true)
    private long id;

    @NotBlank
    @NotNull
    @ApiParam(required = true)
    private String title;

    @NotBlank
    @NotNull
    @ApiParam(required = true)
    private String content;

    @NotNull
    @ApiParam(required = true)
    private int ordinalNumber;

    @NotNull
    @ApiParam(required = true)
    private VaccineType vaccineType;

    @ApiParam(hidden = true)
    private List<ImageDto> attachedImage = new ArrayList<>();

    @Builder
    public ModifyPostRequest(long id, String title, String content, int ordinalNumber, VaccineType vaccineType, List<ImageDto> attachedImage) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ordinalNumber = ordinalNumber;
        this.vaccineType = vaccineType;
        this.attachedImage = attachedImage;
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
