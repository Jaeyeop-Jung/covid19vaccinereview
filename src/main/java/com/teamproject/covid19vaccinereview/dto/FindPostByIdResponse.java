package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostLike;
import com.teamproject.covid19vaccinereview.domain.User;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FindPostByIdResponse {

    @NotNull
    private String writer;

    private String writerProfileImageUrl;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String content;

    private List<String> postImageUrlList = new ArrayList<>();

    @NotNull
    @Min(0)
    private int viewCount;

    @NotNull
    @Min(0)
    private int likeCount;

    @NotNull
    private LocalDateTime dateCreated;

    @NotNull
    private boolean isThisUserLike;

    @Builder
    public FindPostByIdResponse(String writer, String writerProfileImageUrl, String title, String content, List<String> postImageUrlList, int viewCount, int likeCount, LocalDateTime dateCreated, boolean isThisUserLike) {
        this.writer = writer;
        this.writerProfileImageUrl = writerProfileImageUrl;
        this.title = title;
        this.content = content;
        this.postImageUrlList = postImageUrlList;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dateCreated = dateCreated;
        this.isThisUserLike = isThisUserLike;
    }

    public static FindPostByIdResponse of(Post post, String domainUrl, User user){

        FindPostByIdResponse build = FindPostByIdResponse.builder()
                .writer(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .postImageUrlList(
                        post.getPostImageList().stream()
                                .map(postImage -> domainUrl + "/postimage/" + postImage.getFileName())
                                .collect(Collectors.toList())
                )
                .viewCount(post.getViewCount())
                .likeCount(post.getPostLikeList().size())
                .dateCreated(post.getDateCreated())
                .build();

        if(post.getUser().getProfileImage() != null){
            build.writerProfileImageUrl = domainUrl + "/profileimage/" + post.getUser().getProfileImage().getId();
        }

        for (PostLike postLike : post.getPostLikeList()) {

            if(postLike.getUser().equals(user)){
                build.isThisUserLike = true;
                break;
            }

        }

        return build;
    }



}
