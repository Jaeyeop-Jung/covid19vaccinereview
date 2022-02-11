package com.teamproject.covid19vaccinereview.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARDS_ID")
    private Board board;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @NotNull
    private String title;

    @NotNull
    @Lob
    private String content;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImageList = new ArrayList<>();

    @NotNull
    @Column(name = "VIEW_COUNT")
    private int viewCount = 0;

    @NotNull
    @Column(name = "LIKE_COUNT")
    private int likeCount = 0;

    public Post(User user, Board board, String title, String content) {
        this.user = user;
        this.board = board;
        this.title = title;
        this.content = content;
    }

    public static Post of(User user, Board board, String title, String content){
        return new Post(user, board, title, content);
    }

    public void updateViewCount(){
        this.viewCount += 1;
    }

    public void increaseLikeCount(){
        this.likeCount += 1;
    }

    public void changeBoard(Board board){
        this.board = board;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
