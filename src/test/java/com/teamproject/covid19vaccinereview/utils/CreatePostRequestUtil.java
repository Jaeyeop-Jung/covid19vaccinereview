package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.dto.ModifyPostRequest;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import org.springframework.stereotype.Component;

@Component
public class CreatePostRequestUtil {

    public PostWriteRequest createPostWriteRequestWithUUID(String UUID, VaccineType vaccineType, int ordinalNumber){
        return PostWriteRequest.builder()
                .title(UUID)
                .content(UUID)
                .vaccineType(vaccineType)
                .ordinalNumber(ordinalNumber)
                .build();
    }

    public ModifyPostRequest createModifyPostRequest(String title, String content, VaccineType vaccineType, int ordinalNumber, boolean wantToChangePostImage){
        if(title != null && content != null){
            return ModifyPostRequest.builder()
                    .title(title)
                    .content(content)
                    .vaccineType(vaccineType)
                    .ordinalNumber(ordinalNumber)
                    .wantToChangePostImage(wantToChangePostImage)
                    .build();
        } else if(title == null && content != null){
            return ModifyPostRequest.builder()
                    .content(content)
                    .vaccineType(vaccineType)
                    .ordinalNumber(ordinalNumber)
                    .wantToChangePostImage(wantToChangePostImage)
                    .build();
        }
        return null;
    }

}
