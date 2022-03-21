package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import lombok.*;

import java.time.LocalDateTime;
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

    private String writerProfileImageUrl;

    private String title;

    private String content;

    private String postImageUrl;

    private int viewCount;

    private int likeCount;

    private LocalDateTime dateCreated;

    private boolean isThisUserLike;

    @Builder
    public PagingPost(long id, VaccineType vaccineType, int ordinalNumber, String writer, String writerProfileImageUrl, String title, String content, String postImageUrl, int viewCount, int likeCount, LocalDateTime dateCreated, boolean isThisUserLike) {
        this.id = id;
        this.vaccineType = vaccineType;
        this.ordinalNumber = ordinalNumber;
        this.writer = writer;
        this.writerProfileImageUrl = writerProfileImageUrl;
        this.title = title;
        this.content = content;
        this.postImageUrl = postImageUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dateCreated = dateCreated;
        this.isThisUserLike = isThisUserLike;
    }

    public static List<PagingPost> convertOf(String domainUrl, List<Post> postList, List<Post> postLikeList){

        List<PagingPost> response = new ArrayList<>();
        for (Post post : postList) {

            PagingPost build = PagingPost.builder()
                    .id(post.getId())
                    .vaccineType(post.getBoard().getVaccineType())
                    .ordinalNumber(post.getBoard().getOrdinalNumber())
                    .writer(post.getUser().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getPostLikeList().size())
                    .dateCreated(post.getDateCreated())
                    .build();

            if(postLikeList.contains(post)){
                build.isThisUserLike = true;
            }
            if(post.getUser().getProfileImage() != null){
                build.writerProfileImageUrl = domainUrl + "/profileimage/" + post.getUser().getProfileImage().getId();
            }
            if(!post.getPostImageList().isEmpty()){
                build.postImageUrl = domainUrl + "/postimage/" + post.getPostImageList().get(0).getFileName();
            }

            response.add(build);
        }

        return response;
    }
}
