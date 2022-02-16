package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PagingPostResponse {

    private String postUrl;

    private VaccineType vaccineType;

    private int ordinalNumber;

    private String writer;

    private String title;

    private int viewCount;

    private int likeCount;

    @Builder
    public PagingPostResponse(String postUrl, VaccineType vaccineType, int ordinalNumber, String writer, String title, int viewCount, int likeCount) {
        this.postUrl = postUrl;
        this.vaccineType = vaccineType;
        this.ordinalNumber = ordinalNumber;
        this.writer = writer;
        this.title = title;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static Map<String, Object> convertFrom(Map<String, Object> response, List<Post> postList, String domainUrl){

        int count = 1;
        for (Post post : postList) {
            response.put(String.valueOf(count++), PagingPostResponse.builder()
                            .postUrl(domainUrl + "/post/" + post.getId())
                            .vaccineType(post.getBoard().getVaccineType())
                            .ordinalNumber(post.getBoard().getOrdinalNumber())
                            .writer(post.getUser().getNickname())
                            .title(post.getTitle())
                            .viewCount(post.getViewCount())
                            .likeCount(post.getLikeCount())
                            .build()
            );
        }

        return response;
    }
}
