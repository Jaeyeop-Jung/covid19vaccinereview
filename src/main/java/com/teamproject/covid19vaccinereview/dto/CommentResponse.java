package com.teamproject.covid19vaccinereview.dto;

import com.teamproject.covid19vaccinereview.domain.Comment;
import com.teamproject.covid19vaccinereview.domain.CommentLike;
import com.teamproject.covid19vaccinereview.domain.User;
import lombok.*;

import java.time.LocalDateTime;
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

    private String writerProfileImageUrl;

    private String content;

    private int likeCount;

    private List<CommentResponse> children;

    private LocalDateTime dateCreated;

    private boolean isThisUserLike;

    @Builder
    public CommentResponse(long id, String writer, String writerProfileImageUrl, String content, int likeCount, List<CommentResponse> children, LocalDateTime dateCreated, boolean isThisUserLike) {
        this.id = id;
        this.writer = writer;
        this.writerProfileImageUrl = writerProfileImageUrl;
        this.content = content;
        this.likeCount = likeCount;
        this.children = children;
        this.dateCreated = dateCreated;
        this.isThisUserLike = isThisUserLike;
    }

    public static CommentResponse toDto(String domainUrl, Comment comment, User user){

        CommentResponse build = CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .children(new ArrayList<>())
                .likeCount(comment.getCommentLikeList().size())
                .dateCreated(comment.getDateCreated())
                .build();

        if(!comment.isDeleted()){
            build.writer = comment.getUser().getNickname();
            build.writerProfileImageUrl = (comment.getUser().getProfileImage() != null) ?
                    domainUrl + "/profileimage/" + comment.getUser().getProfileImage().getId() : null;
        }

        for (CommentLike commentLike : comment.getCommentLikeList()) {

            if(commentLike.getUser().equals(user)){
                build.isThisUserLike = true;
            }

        }

        return build;
    }

    public static List<CommentResponse> toResponseList(String domainUrl, List<Comment> commentList, User user){

        Map<Comment, CommentResponse> map = new HashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        for (Comment comment : commentList) {

            CommentResponse commentToDto = toDto(domainUrl, comment, user);
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
