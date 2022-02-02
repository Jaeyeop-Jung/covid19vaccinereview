package com.teamproject.covid19vaccinereview.domain;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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


}
