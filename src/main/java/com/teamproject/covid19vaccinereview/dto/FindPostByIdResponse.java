package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FindPostByIdResponse {

    @NotNull
    private String writer;

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

    @Builder
    public FindPostByIdResponse(String writer, String title, String content, List<String> postImageUrlList, int viewCount, int likeCount) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.postImageUrlList = postImageUrlList;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
