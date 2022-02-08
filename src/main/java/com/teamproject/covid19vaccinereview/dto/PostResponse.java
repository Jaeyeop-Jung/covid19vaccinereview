package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostResponse {

    @NotNull
    private String writer;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String content;

    @NotNull
    @Min(0)
    private int viewCount;

    @NotNull
    @Min(0)
    private int likeCount;

    @Builder
    public PostResponse(String writer, String title, String content, int viewCount, int likeCount) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
