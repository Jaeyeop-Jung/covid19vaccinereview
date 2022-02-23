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

    private int likeCount;

    private List<CommentResponse> children;

    @Builder
    private CommentResponse(long id, String writer, String content, int likeCount, List<CommentResponse> children) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.likeCount = likeCount;
        this.children = children;
    }

    public static CommentResponse toDto(Comment comment){

        CommentResponse build = CommentResponse.builder()
                .id(comment.getId())
                .children(new ArrayList<>())
                .likeCount(comment.getCommentLikeList().size())
                .build();

        if(!comment.isDeleted()){
            build.writer = comment.getUser().getNickname();
        }

        return build;
    }

    public static List<CommentResponse> toResponseList(List<Comment> commentList){

        Map<Comment, CommentResponse> map = new HashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        for (Comment comment : commentList) {

            CommentResponse commentToDto = toDto(comment);
            map.put(comment, commentToDto);

            if(comment.hasParent()){

                Comment parent = comment.getParent(); // Depth가 1인 대댓글
                while(true){
                    if(parent.hasParent()){
                        parent = parent.getParent();
                    } else {
                        break;
                    }
                }

//                Comment parent = comment.getParent(); // Depth가 무한대인 대댓글

                CommentResponse parentDto = map.get(parent);
                parentDto.getChildren().add(commentToDto);

            } else {
                roots.add(commentToDto);
            }
        }

        return roots;
    }

}
