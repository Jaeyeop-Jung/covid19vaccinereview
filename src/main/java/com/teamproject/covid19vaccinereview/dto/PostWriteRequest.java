package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWriteRequest {

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
    public PostWriteRequest(String title, String content, int ordinalNumber, VaccineType vaccineType) {
        this.title = title;
        this.content = content;
        this.ordinalNumber = ordinalNumber;
        this.vaccineType = vaccineType;
    }

    public void initPostWriteRequestDto(List<MultipartFile> multipartFileList) {

        for (MultipartFile multipartFile : multipartFileList) {

            String fileExtension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            String fileName;

            if(isSavedFile(multipartFile.getOriginalFilename())){
                fileName = multipartFile.getOriginalFilename().substring(0, lastSecondIndexOf(multipartFile.getOriginalFilename()))
                        + "."
                        + UUID.randomUUID().toString()
                        + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            } else {
                fileName = multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf(".")) + "." + UUID.randomUUID().toString() + fileExtension;
            }

            attachedImage.add(
                    ImageDto.builder()
                            .multipartFile(multipartFile)
                            .fileName(fileName)
                            .fileSize(multipartFile.getSize())
                            .fileExtension(fileExtension)
                            .build()
            );

        }

    }

    public boolean isSavedFile(String fileName){

        int lastIndexOf = fileName.lastIndexOf(".");
        int lastSecondIndexOf = lastSecondIndexOf(fileName);

        if( (lastIndexOf - lastSecondIndexOf) == 37){
            return true;
        } else {
            return false;
        }
    }

    public int lastSecondIndexOf(String fileName){
        int count = 0;
        for(int i=fileName.length()-1 ; i>=0; i--){

            if(fileName.charAt(i) == '.'){
                count ++;
            }

            if(count == 2){
                return i;
            }
        }

        return -1;
    }
}
