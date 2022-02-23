package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FindAllPostResponse {

    int totalPage;

    List<PagingPost> pagingPostList = new ArrayList<>();

    @Builder
    public FindAllPostResponse(int totalPage, List<PagingPost> pagingPostList) {
        this.totalPage = totalPage;
        this.pagingPostList = pagingPostList;
    }
}
