package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Comment;

import java.util.List;

public class CommentResponse {

    private long id;

    private String writer;

    private String content;

    private List<CommentResponse> children;

//    public static List<CommentResponse> toResponseList(List<Comment> commentList){
//
//
//    }
}
