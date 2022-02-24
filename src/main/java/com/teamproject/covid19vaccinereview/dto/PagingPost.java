package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostLike;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private boolean isThisUserLike;

    @Builder
    public PagingPost(long id, VaccineType vaccineType, int ordinalNumber, String writer, String title, int viewCount, int likeCount, boolean isThisUserLike) {
        this.id = id;
        this.vaccineType = vaccineType;
        this.ordinalNumber = ordinalNumber;
        this.writer = writer;
        this.title = title;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isThisUserLike = isThisUserLike;
    }

    public static List<PagingPost> convertOf(List<Post> postList, List<Post> postLikeList){

        List<PagingPost> response = new ArrayList<>();
        for (Post post : postList) {

            PagingPost build = PagingPost.builder()
                    .id(post.getId())
                    .vaccineType(post.getBoard().getVaccineType())
                    .ordinalNumber(post.getBoard().getOrdinalNumber())
                    .writer(post.getUser().getNickname())
                    .title(post.getTitle())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getPostLikeList().size())
                    .build();

            if(postLikeList.contains(post)){
                build.isThisUserLike = true;
            }

            response.add(build);
        }

        return response;
    }
}
