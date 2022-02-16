package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FindPostResponse {

    int totalPage;

    List<PagingPost> pagingPostList = new ArrayList<>();

    @Builder
    public FindPostResponse(int totalPage, List<PagingPost> pagingPostList) {
        this.totalPage = totalPage;
        this.pagingPostList = pagingPostList;
    }
}
