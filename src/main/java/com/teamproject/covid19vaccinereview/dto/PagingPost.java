package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PagingPost {

    private long id; // X -> ID만 넘기기,

    private VaccineType vaccineType;

    private int ordinalNumber;

    private String writer;

    private String title;

    private int viewCount;

    private int likeCount;

    @Builder
    public PagingPost(long id, VaccineType vaccineType, int ordinalNumber, String writer, String title, int viewCount, int likeCount) {
        this.id = id;
        this.vaccineType = vaccineType;
        this.ordinalNumber = ordinalNumber;
        this.writer = writer;
        this.title = title;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static List<PagingPost> convertFrom(List<Post> postList){

        List<PagingPost> response = new ArrayList<>();
        for (Post post : postList) {
            response.add(
                    PagingPost.builder()
                            .id(post.getId())
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
