package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
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

}
