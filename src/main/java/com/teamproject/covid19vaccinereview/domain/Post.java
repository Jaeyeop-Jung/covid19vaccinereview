package com.teamproject.covid19vaccinereview.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Clob;
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

    @NotNull
    @Column(name = "LIKE_NUMBER")
    private int likeNumber;

    public Post(User user, Board board, String title, String content, int likeNumber) {
        this.user = user;
        this.board = board;
        this.title = title;
        this.content = content;
        this.likeNumber = likeNumber;
    }

    public static Post of(User user, Board board, String title, String content, int likeNumber){
        return new Post(user, board, title, content, likeNumber);
    }

}
