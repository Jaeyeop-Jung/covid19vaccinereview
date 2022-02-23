package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Comment;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CommentResponse {

    private long id;

    private String writer;

    private String content;

    private List<CommentResponse> children;

    @Builder
    public CommentResponse(long id, String writer, String content, List<CommentResponse> children) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.children = children;
    }

    public static CommentResponse toDto(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .writer(comment.getUser().getNickname())
                .content(comment.getContent())
                .children(new ArrayList<>())
                .build();
    }

    public static List<CommentResponse> toResponseList(List<Comment> commentList){

        Map<Comment, CommentResponse> map = new HashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentResponse commentToDto = toDto(comment);
            map.put(comment, commentToDto);

            if(comment.hasParent()){

                Comment parent = comment.getParent();
                CommentResponse parentDto = map.get(parent);
                parentDto.getChildren().add(commentToDto);

            } else {
                roots.add(commentToDto);
            }
        }

        return roots;
    }
}
