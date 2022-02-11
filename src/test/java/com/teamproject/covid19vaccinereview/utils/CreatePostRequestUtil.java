package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.dto.ModifyPostRequest;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

    public ModifyPostRequest createModifyPostRequest(String UUID,VaccineType vaccineType, int ordinalNumber, boolean wantToChangePostImage){
        return ModifyPostRequest.builder()
                .title(UUID)
                .content(UUID)
                .vaccineType(vaccineType)
                .ordinalNumber(ordinalNumber)
                .wantToChangePostImage(wantToChangePostImage)
                .build();
    }

    public Map<Object, Object> createModifyPostRequestOnlyTitle(String title, VaccineType vaccineType, int ordinalNumber, boolean wantToChangePostImage){
        HashMap<Object, Object> map = new HashMap<>();

        map.put("title", title);
        map.put("vaccineType", vaccineType);
        map.put("ordinalNumber", ordinalNumber);
        map.put("wantToChangePostImage", wantToChangePostImage);

        return map;
    }

    public Map<Object, Object> createModifyPostRequestOnlyContent(String content, VaccineType vaccineType, int ordinalNumber, boolean wantToChangePostImage){
        HashMap<Object, Object> map = new HashMap<>();

        map.put("content", content);
        map.put("vaccineType", vaccineType);
        map.put("ordinalNumber", ordinalNumber);
        map.put("wantToChangePostImage", wantToChangePostImage);

        return map;
    }

    public Map<Object, Object> createModifyPostRequestOnlyBoard(VaccineType vaccineType, int ordinalNumber, boolean wantToChangePostImage){
        HashMap<Object, Object> map = new HashMap<>();

        map.put("vaccineType", vaccineType);
        map.put("ordinalNumber", ordinalNumber);
        map.put("wantToChangePostImage", wantToChangePostImage);

        return map;
    }

}
