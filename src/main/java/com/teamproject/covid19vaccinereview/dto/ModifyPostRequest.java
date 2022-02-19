package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.aop.exception.customException.IncorrectModifyFileNameException;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyPostRequest {

    private String title;

    private String content;

    @NotNull
    @ApiParam(required = true)
    private int ordinalNumber;

    @NotNull
    @ApiParam(required = true)
    private VaccineType vaccineType;

    @NotNull
    @ApiParam(required = true)
    private boolean wantToChangePostImage;

    private List<String> modifyPostImageList;

    @ApiParam(hidden = true)
    private List<ImageDto> attachedImage = new ArrayList<>();

    @Builder
    public ModifyPostRequest(String title, String content, int ordinalNumber, VaccineType vaccineType, boolean wantToChangePostImage, List<String> modifyPostImageList) {
        this.title = title;
        this.content = content;
        this.ordinalNumber = ordinalNumber;
        this.vaccineType = vaccineType;
        this.wantToChangePostImage = wantToChangePostImage;
        this.modifyPostImageList = modifyPostImageList;
    }

    public void initPostWriteRequestDto(List<MultipartFile> multipartFileList) {

        for (MultipartFile multipartFile : multipartFileList) {

            if(!this.modifyPostImageList.contains(multipartFile.getOriginalFilename())){
                throw new IncorrectModifyFileNameException("");
            }

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
            modifyPostImageList.set(modifyPostImageList.indexOf(multipartFile.getOriginalFilename()), fileName);
        }
    }

    public boolean isSavedFile(String fileName){

        int minusLastIndexOf = fileName.lastIndexOf(".") - lastSecondIndexOf(fileName);

        if( minusLastIndexOf == 37){
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
