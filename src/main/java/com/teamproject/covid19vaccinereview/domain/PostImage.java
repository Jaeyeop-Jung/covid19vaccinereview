package com.teamproject.covid19vaccinereview.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "POSTIMAGE")
public class PostImage extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTIMAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @NotNull
    @Column(name = "file_name")
    private String fileName;

    @NotNull
    @Column(name = "file_size")
    private Long fileSize;

    @NotNull
    @Column(name = "file_extension")
    private String fileExtension;
}
