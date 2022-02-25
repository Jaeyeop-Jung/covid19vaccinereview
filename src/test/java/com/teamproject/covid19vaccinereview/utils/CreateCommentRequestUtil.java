package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateCommentRequestUtil {

    public CommentWriteRequest createCommentWriteRequest(String content, long parentId){
        return CommentWriteRequest.builder()
                .content(content)
                .parentId(parentId)
                .build();
    }

}
